# Hotel Management System — ERD v3

Corrected version: multi-room bookings, no redundant hotel_id, merged stay record, price snapshots.

```mermaid
erDiagram
  ROLES ||--o{ USERS : has
  USERS ||--o| EMPLOYEES : "linked to"
  USERS ||--o| CUSTOMERS : "optional login"
  USERS ||--o{ NOTIFICATIONS : receives
  USERS ||--o{ BOOKINGS : "created by (nullable)"
  HOTELS ||--o{ EMPLOYEES : employs
  HOTELS ||--o{ ROOM_TYPES : offers
  HOTELS ||--o{ HOTEL_SERVICES : provides
  ROOM_TYPES ||--o{ ROOMS : classifies
  CUSTOMERS ||--o{ BOOKINGS : makes
  BOOKINGS ||--|{ BOOKING_ROOMS : reserves
  ROOMS ||--o{ BOOKING_ROOMS : "reserved in"
  BOOKINGS ||--o| STAYS : "becomes"
  EMPLOYEES ||--o{ STAYS : "handles check-in/out"
  BOOKINGS ||--o{ PAYMENTS : paid_by
  BOOKINGS ||--o{ BOOKING_SERVICES : includes
  HOTEL_SERVICES ||--o{ BOOKING_SERVICES : ordered
  ROOMS ||--o{ HOUSEKEEPING_TASKS : needs
  EMPLOYEES ||--o{ HOUSEKEEPING_TASKS : assigned

  ROLES {
    bigint id PK
    string name UK
    string description
  }
  USERS {
    bigint id PK
    bigint role_id FK
    string username UK
    string email UK
    string password_hash
    boolean enabled
    timestamp created_at
  }
  EMPLOYEES {
    bigint id PK
    bigint user_id FK "nullable, unique"
    bigint hotel_id FK
    string full_name
    string phone
    string position
    decimal salary
    date hire_date
  }
  HOTELS {
    bigint id PK
    string name
    string address
    string city
    string phone
    string email
    int star_rating
  }
  ROOM_TYPES {
    bigint id PK
    bigint hotel_id FK
    string name
    int capacity
    decimal price_per_night
    string description
  }
  ROOMS {
    bigint id PK
    bigint room_type_id FK
    string room_number "unique per hotel"
    int floor
    string status "ENUM"
  }
  CUSTOMERS {
    bigint id PK
    bigint user_id FK "nullable, unique"
    string full_name
    string email
    string phone
    string id_number
    string address
  }
  BOOKINGS {
    bigint id PK
    bigint customer_id FK
    bigint created_by FK "nullable"
    date check_in_date
    date check_out_date "must be after check_in_date"
    int guests
    decimal total_amount
    string status "ENUM"
    timestamp created_at
  }
  BOOKING_ROOMS {
    bigint id PK
    bigint booking_id FK
    bigint room_id FK
    decimal price_per_night "snapshot"
  }
  STAYS {
    bigint id PK
    bigint booking_id FK "unique"
    bigint checked_in_by FK "employee"
    bigint checked_out_by FK "employee, nullable"
    timestamp checked_in_at
    timestamp checked_out_at "nullable"
    decimal extra_charges
    string notes
  }
  PAYMENTS {
    bigint id PK
    bigint booking_id FK
    decimal amount
    string currency
    string method "ENUM"
    string status "ENUM"
    string transaction_ref
    timestamp paid_at
  }
  HOTEL_SERVICES {
    bigint id PK
    bigint hotel_id FK
    string name
    decimal price
    boolean available
  }
  BOOKING_SERVICES {
    bigint id PK
    bigint booking_id FK
    bigint hotel_service_id FK
    int quantity
    decimal unit_price "snapshot"
    decimal total_price
  }
  HOUSEKEEPING_TASKS {
    bigint id PK
    bigint room_id FK
    bigint employee_id FK
    string task_type "ENUM"
    string status "ENUM"
    timestamp scheduled_at
    timestamp completed_at
  }
  NOTIFICATIONS {
    bigint id PK
    bigint user_id FK
    string title
    string message
    boolean is_read
    timestamp created_at
  }
```

## What changed from v2

**Structure**

- Removed `hotel_id` from `ROOMS`; hotel comes via `ROOM_TYPES`.
- Added `BOOKING_ROOMS` so one booking can reserve several rooms.
- Merged `CHECK_INS` + `CHECK_OUTS` into `STAYS`, with employee audit fields.
- Added optional `USERS → CUSTOMERS` for online login.

**Data quality**

- Price snapshots: `BOOKING_ROOMS.price_per_night`, `BOOKING_SERVICES.unit_price`.
- `password` → `password_hash`.
- `created_by` is nullable; added `created_at` on bookings.
- Payments gain `currency` and `transaction_ref`.

**Constraints to implement**

- Unique: `users.username`, `users.email`, `roles.name`.
- Unique room number per hotel (enforce via a hotel-scoped check or trigger).
- `check_out_date > check_in_date`.
- No overlapping active bookings for the same room.
- Status/method/task_type fields as ENUMs or lookup tables.
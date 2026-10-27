package com.sakhak.hotel.roomtype;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room-types")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    // GET all room types
    @GetMapping
    public ResponseEntity<List<RoomType>> getAllRoomTypes() {

        return ResponseEntity.ok(
                roomTypeService.getAllRoomTypes()
        );
    }

    // GET room type by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoomType> getRoomTypeById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                roomTypeService.getRoomTypeById(id)
        );
    }

    // CREATE room type
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRoomType(
            @RequestBody RoomTypeDTO dto
    ) {

        RoomType roomType = roomTypeService.createRoomType(dto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Room type created successfully");
        response.put("roomType", roomType);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // UPDATE room type
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRoomType(
            @PathVariable Long id,
            @RequestBody RoomTypeDTO dto
    ) {

        RoomType roomType = roomTypeService.updateRoomType(id, dto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Room type updated successfully");
        response.put("roomType", roomType);

        return ResponseEntity.ok(response);
    }

    // DELETE room type
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRoomType(
            @PathVariable Long id
    ) {

        roomTypeService.deleteRoomType(id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Room type deleted successfully");

        return ResponseEntity.ok(response);
    }
}
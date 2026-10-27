package com.sakhak.hotel.room;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // GET all rooms
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // GET room by ID
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // CREATE room
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRoom(@RequestBody RoomDTO dto) {

        Room room = roomService.createRoom(dto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Room created successfully");
        response.put("room", room);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // UPDATE room
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRoom(
            @PathVariable Long id,
            @RequestBody RoomDTO dto) {

        Room room = roomService.updateRoom(id, dto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Room updated successfully");
        response.put("room", room);

        return ResponseEntity.ok(response);
    }

    // DELETE room
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRoom(@PathVariable Long id) {

        roomService.deleteRoom(id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Room deleted successfully");

        return ResponseEntity.ok(response);
    }
}
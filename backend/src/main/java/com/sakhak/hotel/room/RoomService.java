package com.sakhak.hotel.room;

import com.sakhak.hotel.roomtype.RoomType;
import com.sakhak.hotel.roomtype.RoomTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    public RoomService(RoomRepository roomRepository,
                       RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get room by ID
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    // Create room
    public Room createRoom(RoomDTO dto) {

        RoomType roomType = roomTypeRepository.findById(dto.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        Room room = new Room();

        room.setRoomType(roomType);
        room.setRoomNumber(dto.getRoomNumber());
        room.setFloor(dto.getFloor());
        room.setStatus(dto.getStatus());
        room.setDescription(dto.getDescription());

        return roomRepository.save(room);
    }

    // Update room
    public Room updateRoom(Long id, RoomDTO dto) {

        Room room = getRoomById(id);

        RoomType roomType = roomTypeRepository.findById(dto.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        room.setRoomType(roomType);
        room.setRoomNumber(dto.getRoomNumber());
        room.setFloor(dto.getFloor());
        room.setStatus(dto.getStatus());
        room.setDescription(dto.getDescription());

        return roomRepository.save(room);
    }

    // Delete room
    public void deleteRoom(Long id) {

        Room room = getRoomById(id);

        roomRepository.delete(room);
    }
}
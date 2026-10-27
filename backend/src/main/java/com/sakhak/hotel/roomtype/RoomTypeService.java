package com.sakhak.hotel.roomtype;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    // Get all room types
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    // Get room type by ID
    public RoomType getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room type not found"));
    }

    // Create room type
    public RoomType createRoomType(RoomTypeDTO dto) {

        RoomType roomType = new RoomType();

        roomType.setName(dto.getName());
        roomType.setDescription(dto.getDescription());
        roomType.setPricePerNight(dto.getPricePerNight());

        return roomTypeRepository.save(roomType);
    }

    // Update room type
    public RoomType updateRoomType(Long id, RoomTypeDTO dto) {

        RoomType roomType = getRoomTypeById(id);

        roomType.setName(dto.getName());
        roomType.setDescription(dto.getDescription());
        roomType.setPricePerNight(dto.getPricePerNight());

        return roomTypeRepository.save(roomType);
    }

    // Delete room type
    public void deleteRoomType(Long id) {

        RoomType roomType = getRoomTypeById(id);

        roomTypeRepository.delete(roomType);
    }
}
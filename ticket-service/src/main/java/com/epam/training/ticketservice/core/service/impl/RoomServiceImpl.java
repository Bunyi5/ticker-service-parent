package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.RoomRepository;
import com.epam.training.ticketservice.core.persistence.entity.Room;
import com.epam.training.ticketservice.core.service.RoomService;
import com.epam.training.ticketservice.core.service.model.RoomDto;
import com.epam.training.ticketservice.ui.command.model.RoomDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void createRoom(RoomDto roomDto) {
        saveRoom(null, roomDto);
    }

    @Override
    public void updateRoom(RoomDto roomDto) {
        Optional<Room> maybeRoom = roomRepository.findByRoomName(roomDto.getRoomName());
        if (maybeRoom.isEmpty()) {
            throw new NoSuchElementException("Room not found!");
        } else {
            saveRoom(maybeRoom.get().getId(), roomDto);
        }
    }

    @Override
    public void deleteRoom(String roomName) {
        Objects.requireNonNull(roomName, "Room Name cannot be null");
        roomRepository.deleteByRoomName(roomName);
    }

    @Override
    public RoomDtoList getRoomList() {
        return new RoomDtoList(getRoomDtoList());
    }

    private void saveRoom(Long id, RoomDto roomDto) {
        checkRoomDto(roomDto);
        Room room = new Room(id,
                roomDto.getRoomName(),
                roomDto.getRoomSeat(),
                roomDto.getRoomRow(),
                roomDto.getRoomColumn());
        roomRepository.save(room);
    }

    private void checkRoomDto(RoomDto roomDto) {
        Objects.requireNonNull(roomDto, "Room cannot be null");
        Objects.requireNonNull(roomDto.getRoomName(), "Room Name cannot be null");
        if (roomDto.getRoomRow() <= 0) {
            throw new NullPointerException("Room Row cannot be zero or less");
        }
        if (roomDto.getRoomColumn() <= 0) {
            throw new NullPointerException("Room Column cannot be zero or less");
        }
    }

    private List<RoomDto> getRoomDtoList() {
        return roomRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private RoomDto convertEntityToDto(Room room) {
        return RoomDto.builder()
                .roomName(room.getRoomName())
                .roomRow(room.getRoomRow())
                .roomColumn(room.getRoomColumn())
                .build();
    }
}

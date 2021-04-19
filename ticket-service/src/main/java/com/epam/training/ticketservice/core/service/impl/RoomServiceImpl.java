package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.RoomRepository;
import com.epam.training.ticketservice.core.persistence.entity.Room;
import com.epam.training.ticketservice.core.service.RoomService;
import com.epam.training.ticketservice.ui.command.model.RoomList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void createRoom(String roomName, int roomRow, int roomColumn) {
        Room room = new Room(null, roomName, roomRow * roomColumn, roomRow, roomColumn);
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(String roomName, int roomRow, int roomColumn) {
        Optional<Room> maybeRoom = roomRepository.findByRoomName(roomName);
        maybeRoom.ifPresentOrElse(
            (room) -> {
                room.setRoomName(roomName);
                room.setRoomSeat(roomRow * roomColumn);
                room.setRoomRow(roomRow);
                room.setRoomColumn(roomColumn);
                roomRepository.save(room);
            }, () -> {
                throw new NoSuchElementException("Room not found!");
            });
    }

    @Override
    public void deleteRoom(String roomName) {
        roomRepository.deleteByRoomName(roomName);
    }

    @Override
    public RoomList getRoomList() {
        return new RoomList(roomRepository.findAll());
    }
}

package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.core.service.model.RoomDto;
import com.epam.training.ticketservice.ui.command.model.RoomDtoList;

public interface RoomService {

    void createRoom(RoomDto roomDto);

    void updateRoom(RoomDto roomDto);

    void deleteRoom(String name);

    RoomDtoList getRoomDtoList();

    RoomDto getRoomDtoByRoomName(String roomName);
}

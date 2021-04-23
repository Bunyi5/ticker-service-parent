package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.model.RoomDtoList;

public interface RoomService {

    void createRoom(RoomDto roomDto);

    void updateRoom(RoomDto roomDto);

    void deleteRoom(String name);

    RoomDtoList getRoomDtoList();

    RoomDto getRoomDtoByRoomName(String roomName);
}

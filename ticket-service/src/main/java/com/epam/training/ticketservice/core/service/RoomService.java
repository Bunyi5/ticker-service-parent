package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.ui.command.model.RoomList;

public interface RoomService {

    void createRoom(String name, int row, int column);

    void updateRoom(String name, int row, int column);

    void deleteRoom(String name);

    RoomList getRoomList();
}

package com.epam.training.ticketservice.ui.command.model;

import com.epam.training.ticketservice.core.persistence.entity.Room;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode
@RequiredArgsConstructor
public class RoomList {

    private final List<Room> roomList;

    @Override
    public String toString() {
        if (roomList.isEmpty()) {
            return "There are no rooms at the moment";
        } else {
            return roomList.stream().map(Objects::toString).collect(Collectors.joining("\n"));
        }
    }
}

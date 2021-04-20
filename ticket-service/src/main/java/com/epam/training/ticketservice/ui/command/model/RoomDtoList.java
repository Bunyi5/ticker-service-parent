package com.epam.training.ticketservice.ui.command.model;

import com.epam.training.ticketservice.core.service.model.RoomDto;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode
@RequiredArgsConstructor
public class RoomDtoList {

    private final List<RoomDto> roomDtoList;

    @Override
    public String toString() {
        if (roomDtoList.isEmpty()) {
            return "There are no rooms at the moment";
        } else {
            return roomDtoList.stream().map(Objects::toString).collect(Collectors.joining("\n"));
        }
    }
}

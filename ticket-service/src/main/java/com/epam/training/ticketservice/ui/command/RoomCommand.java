package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.service.AvailabilityService;
import com.epam.training.ticketservice.core.service.RoomService;
import com.epam.training.ticketservice.core.service.model.RoomDto;
import com.epam.training.ticketservice.ui.command.model.RoomDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class RoomCommand {

    private final RoomService roomService;
    private final AvailabilityService availabilityService;

    @ShellMethod(value = "Create a room", key = "create room")
    public RoomDto createRoom(@ShellOption String roomName, @ShellOption int roomRow, @ShellOption int roomColumn) {
        RoomDto roomDto = buildRoomDto(roomName, roomRow, roomColumn);
        roomService.createRoom(roomDto);
        return roomDto;
    }

    @ShellMethod(value = "Update a room", key = "update room")
    public RoomDto updateRoom(@ShellOption String roomName, @ShellOption int roomRow, @ShellOption int roomColumn) {
        RoomDto roomDto = buildRoomDto(roomName, roomRow, roomColumn);
        roomService.updateRoom(roomDto);
        return roomDto;
    }

    @ShellMethod(value = "Delete a room", key = "delete room")
    public void deleteRoom(@ShellOption String roomName) {
        roomService.deleteRoom(roomName);
    }

    @ShellMethod(value = "List rooms", key = "list rooms")
    public RoomDtoList listRooms() {
        return roomService.getRoomDtoList();
    }

    @ShellMethodAvailability({"create room", "update room", "delete room"})
    public Availability isSignedInAccountAdmin() {
        return availabilityService.isSignedInAccountAdmin();
    }

    private RoomDto buildRoomDto(String roomName, int roomRow, int roomColumn) {
        return RoomDto.builder()
                .roomName(roomName)
                .roomRow(roomRow)
                .roomColumn(roomColumn)
                .build();
    }
}

package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.service.AvailabilityService;
import com.epam.training.ticketservice.core.service.RoomService;
import com.epam.training.ticketservice.ui.command.model.RoomList;
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

    @ShellMethodAvailability("isSignedInAccountAdmin")
    @ShellMethod(value = "Create a room", key = "create room")
    public void createRoom(@ShellOption String roomName, @ShellOption int roomRow, @ShellOption int roomColumn) {
        roomService.createRoom(roomName, roomRow, roomColumn);
    }

    @ShellMethodAvailability("isSignedInAccountAdmin")
    @ShellMethod(value = "Update a room", key = "update room")
    public void updateRoom(@ShellOption String roomName, @ShellOption int roomRow, @ShellOption int roomColumn) {
        roomService.updateRoom(roomName, roomRow, roomColumn);
    }

    @ShellMethodAvailability("isSignedInAccountAdmin")
    @ShellMethod(value = "Delete a room", key = "delete room")
    public void deleteRoom(@ShellOption String roomName) {
        roomService.deleteRoom(roomName);
    }

    @ShellMethod(value = "List rooms", key = "list rooms")
    public RoomList listRooms() {
        return roomService.getRoomList();
    }

    public Availability isSignedInAccountAdmin() {
        return availabilityService.isSignedInAccountAdmin();
    }
}

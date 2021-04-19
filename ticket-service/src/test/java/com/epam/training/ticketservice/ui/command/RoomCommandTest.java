package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.persistence.entity.Room;
import com.epam.training.ticketservice.core.service.AvailabilityService;
import com.epam.training.ticketservice.core.service.RoomService;
import com.epam.training.ticketservice.ui.command.model.RoomList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.shell.Availability;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RoomCommandTest {

    private RoomCommand roomCommand;

    @Mock
    private RoomService roomService;
    @Mock
    private AvailabilityService availabilityService;

    @BeforeEach
    public void init() {
        roomCommand = new RoomCommand(roomService, availabilityService);
    }

    @Test
    public void testCreateRoomShouldCallRoomServiceWithTheRightParameters() {
        // Given
        String title = "Pedersoli";
        int row = 20;
        int column = 10;

        // When
        roomCommand.createRoom(title, row, column);

        // Then
        Mockito.verify(roomService).createRoom(title, row, column);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void testUpdateRoomShouldCallRoomServiceWithTheRightParameters() {
        // Given
        String title = "Pedersoli";
        int row = 20;
        int column = 10;

        // When
        roomCommand.updateRoom(title, row, column);

        // Then
        Mockito.verify(roomService).updateRoom(title, row, column);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void testDeleteRoomShouldCallRoomServiceWithTheRightParameters() {
        // Given
        String title = "Pedersoli";

        // When
        roomCommand.deleteRoom(title);

        // Then
        Mockito.verify(roomService).deleteRoom(title);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void testListRoomsShouldReturnExpectedResult() {
        // Given
        int row = 20;
        int column = 10;
        Room room = new Room(null, "Pedersoli", row * column, row, column);
        RoomList expected = new RoomList(List.of(room));

        Mockito.when(roomService.getRoomList())
                .thenReturn(expected);

        // When
        RoomList actual = roomCommand.listRooms();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(roomService).getRoomList();
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void testIsSignedInAccountAdminShouldReturnsExpected() {
        // Given
        Availability expected = Availability.available();

        Mockito.when(availabilityService.isSignedInAccountAdmin())
                .thenReturn(expected);

        // When
        Availability actual = roomCommand.isSignedInAccountAdmin();

        // Then
        Assertions.assertEquals(expected.isAvailable(), actual.isAvailable());
        Mockito.verify(availabilityService).isSignedInAccountAdmin();
        Mockito.verifyNoMoreInteractions(availabilityService);
    }
}
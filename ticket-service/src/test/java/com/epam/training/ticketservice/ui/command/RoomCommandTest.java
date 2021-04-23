package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.service.AvailabilityService;
import com.epam.training.ticketservice.core.service.RoomService;
import com.epam.training.ticketservice.core.service.model.RoomDto;
import com.epam.training.ticketservice.ui.command.model.RoomDtoList;
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

    private static final String ROOM_NAME = "Pedersoli";
    private static final int ROOM_ROW = 20;
    private static final int ROOM_COLUMN = 10;
    private static final RoomDto ROOM_DTO = RoomDto.builder()
            .roomName(ROOM_NAME)
            .roomRow(ROOM_ROW)
            .roomColumn(ROOM_COLUMN)
            .build();

    @BeforeEach
    public void init() {
        roomCommand = new RoomCommand(roomService, availabilityService);
    }

    @Test
    public void testCreateRoomShouldCallRoomServiceWithTheRightParameters() {
        // Given
        Mockito.doNothing().when(roomService).createRoom(ROOM_DTO);

        // When
        RoomDto actual = roomCommand.createRoom(ROOM_NAME, ROOM_ROW, ROOM_COLUMN);

        // Then
        Assertions.assertEquals(ROOM_DTO, actual);
        Mockito.verify(roomService).createRoom(ROOM_DTO);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void testUpdateRoomShouldCallRoomServiceWithTheRightParameters() {
        // Given
        Mockito.doNothing().when(roomService).updateRoom(ROOM_DTO);

        // When
        RoomDto actual = roomCommand.updateRoom(ROOM_NAME, ROOM_ROW, ROOM_COLUMN);

        // Then
        Assertions.assertEquals(ROOM_DTO, actual);
        Mockito.verify(roomService).updateRoom(ROOM_DTO);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void testDeleteRoomShouldCallRoomServiceWithTheRightParameters() {
        // Given
        Mockito.doNothing().when(roomService).deleteRoom(ROOM_NAME);

        // When
        roomCommand.deleteRoom(ROOM_NAME);

        // Then
        Mockito.verify(roomService).deleteRoom(ROOM_NAME);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void testListRoomsShouldReturnExpectedResult() {
        // Given
        RoomDtoList expected = new RoomDtoList(List.of(ROOM_DTO));

        Mockito.when(roomService.getRoomDtoList())
                .thenReturn(expected);

        // When
        RoomDtoList actual = roomCommand.listRooms();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(roomService).getRoomDtoList();
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
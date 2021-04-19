package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.RoomRepository;
import com.epam.training.ticketservice.core.persistence.entity.Room;
import com.epam.training.ticketservice.core.service.RoomService;
import com.epam.training.ticketservice.ui.command.model.RoomList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    public void init() {
        roomService = new RoomServiceImpl(roomRepository);
    }

    @Test
    public void testCreateRoomShouldCallRoomRepositoryWithTheRightParameters() {
        // Given
        int row = 20;
        int column = 10;
        Room expected = new Room(null, "Pedersoli", row * column, row, column);

        // When
        roomService.createRoom(expected.getRoomName(), expected.getRoomRow(), expected.getRoomColumn());

        // Then
        Mockito.verify(roomRepository).save(expected);
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testUpdateRoomShouldCallRoomRepositoryWithTheRightParameters() {
        // Given
        int row = 20;
        int column = 10;
        Room expected = new Room(null, "Pedersoli", row * column, row, column);

        Mockito.when(roomRepository.findByRoomName(expected.getRoomName()))
                .thenReturn(Optional.of(new Room()));

        // When
        roomService.updateRoom(expected.getRoomName(), expected.getRoomRow(), expected.getRoomColumn());

        // Then
        Mockito.verify(roomRepository).findByRoomName(expected.getRoomName());
        Mockito.verify(roomRepository).save(expected);
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testUpdateRoomShouldThrowExceptionWhenRoomNotFound() {
        // Given
        String unknownRoomName = "notRoomName";

        Mockito.when(roomRepository.findByRoomName(unknownRoomName))
                .thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roomService.updateRoom(unknownRoomName, 0, 0));

        // Then
        Mockito.verify(roomRepository).findByRoomName(unknownRoomName);
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testDeleteRoomShouldCallRoomRepository() {
        // Given
        String roomName = "Pedersoli";

        // When
        roomService.deleteRoom(roomName);

        // Then
        Mockito.verify(roomRepository).deleteByRoomName(roomName);
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testGetRoomListShouldReturnExpectedResult() {
        // Given
        int row = 20;
        int column = 10;
        Room room = new Room(null, "Pedersoli", row * column, row, column);
        RoomList expected = new RoomList(List.of(room));

        Mockito.when(roomRepository.findAll())
                .thenReturn(List.of(room));

        // When
        RoomList actual = roomService.getRoomList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(roomRepository).findAll();
        Mockito.verifyNoMoreInteractions(roomRepository);
    }
}
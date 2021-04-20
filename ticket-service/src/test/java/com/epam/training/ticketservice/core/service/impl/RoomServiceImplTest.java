package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.RoomRepository;
import com.epam.training.ticketservice.core.persistence.entity.Room;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    private static final String ROOM_NAME = "Pedersoli";
    private static final int ROOM_ROW = 20;
    private static final int ROOM_COLUMN = 10;
    private static final RoomDto ROOM_DTO = RoomDto.builder()
            .roomName(ROOM_NAME)
            .roomRow(ROOM_ROW)
            .roomColumn(ROOM_COLUMN)
            .build();
    private static final Room ROOM_ENTITY =
            new Room(null, ROOM_NAME, ROOM_ROW * ROOM_COLUMN, ROOM_ROW, ROOM_COLUMN);

    @BeforeEach
    public void init() {
        roomService = new RoomServiceImpl(roomRepository);
    }

    @Test
    public void testCreateRoomShouldCallRoomRepositoryWhenTheInputRoomIsValid() {
        // Given
        Mockito.when(roomRepository.save(ROOM_ENTITY))
                .thenReturn(ROOM_ENTITY);

        // When
        roomService.createRoom(ROOM_DTO);

        // Then
        Mockito.verify(roomRepository).save(ROOM_ENTITY);
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenTheRoomIsNull() {
        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> roomService.createRoom(null));

        // Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenTheRoomNameIsNull() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .roomName(null)
                .roomRow(ROOM_DTO.getRoomRow())
                .roomColumn(ROOM_DTO.getRoomColumn())
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> roomService.createRoom(roomDto));

        // Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenTheRoomRowIsZero() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .roomName(ROOM_DTO.getRoomName())
                .roomRow(0)
                .roomColumn(ROOM_DTO.getRoomColumn())
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> roomService.createRoom(roomDto));

        // Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenTheRoomColumnIsZero() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .roomName(ROOM_DTO.getRoomName())
                .roomRow(ROOM_DTO.getRoomRow())
                .roomColumn(0)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> roomService.createRoom(roomDto));

        // Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testUpdateRoomShouldCallRoomRepositoryWhenTheInputRoomIsValid() {
        // Given
        Mockito.when(roomRepository.findByRoomName(ROOM_ENTITY.getRoomName()))
                .thenReturn(Optional.of(ROOM_ENTITY));

        // When
        roomService.updateRoom(ROOM_DTO);

        // Then
        Mockito.verify(roomRepository).findByRoomName(ROOM_ENTITY.getRoomName());
        Mockito.verify(roomRepository).save(ROOM_ENTITY);
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testUpdateRoomShouldThrowExceptionWhenRoomNotFound() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .roomName("notRoomName")
                .roomRow(ROOM_DTO.getRoomRow())
                .roomColumn(ROOM_DTO.getRoomColumn())
                .build();

        Mockito.when(roomRepository.findByRoomName(roomDto.getRoomName()))
                .thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roomService.updateRoom(roomDto));

        // Then
        Mockito.verify(roomRepository).findByRoomName(roomDto.getRoomName());
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testDeleteRoomShouldCallRoomRepositoryWhenTheInputRoomNameIsValid() {
        // Given
        String roomName = "Pedersoli";

        Mockito.doNothing().when(roomRepository).deleteByRoomName(roomName);

        // When
        roomService.deleteRoom(roomName);

        // Then
        Mockito.verify(roomRepository).deleteByRoomName(roomName);
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testGetRoomListShouldReturnExpectedResult() {
        // Given
        RoomDtoList expected = new RoomDtoList(List.of(ROOM_DTO));

        Mockito.when(roomRepository.findAll())
                .thenReturn(List.of(ROOM_ENTITY));

        // When
        RoomDtoList actual = roomService.getRoomList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(roomRepository).findAll();
        Mockito.verifyNoMoreInteractions(roomRepository);
    }
}
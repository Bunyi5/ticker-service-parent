package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.entity.Movie;
import com.epam.training.ticketservice.core.persistence.entity.Room;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.core.service.RoomService;
import com.epam.training.ticketservice.core.service.model.MovieDto;
import com.epam.training.ticketservice.core.service.model.RoomDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScreeningDetailsProviderTest {

    private ScreeningDetailsProvider screeningDetailsProvider;

    @Mock
    private MovieService movieService;
    @Mock
    private RoomService roomService;

    @BeforeEach
    public void init() {
        screeningDetailsProvider = new ScreeningDetailsProvider(movieService, roomService);
    }

    @Test
    public void testGetMovieByTitleShouldCallMovieServiceAndReturnExpected() {
        // Given
        long id = 10L;
        String title = "Sátántangó";
        String genre = "drama";
        int length = 450;
        MovieDto movieDto = MovieDto.builder()
                .id(id)
                .title(title)
                .genre(genre)
                .length(length)
                .build();
        Movie expected = new Movie(id, title, genre, length);

        Mockito.when(movieService.getMovieDtoByTitle(title))
                .thenReturn(movieDto);

        // When
        Movie actual = screeningDetailsProvider.getMovieByTitle(title);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(movieService).getMovieDtoByTitle(title);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testGetRoomByRoomNameShouldCallRoomServiceAndReturnExpected() {
        // Given
        long id = 20L;
        String roomName = "Pedersoli";
        int roomRow = 20;
        int roomColumn = 10;
        RoomDto roomDto = RoomDto.builder()
                .id(id)
                .roomName(roomName)
                .roomRow(roomRow)
                .roomColumn(roomColumn)
                .build();
        Room expected = new Room(id, roomName,
                roomRow * roomColumn, roomRow, roomColumn);

        Mockito.when(roomService.getRoomDtoByRoomName(roomName))
                .thenReturn(roomDto);

        // When
        Room actual = screeningDetailsProvider.getRoomByRoomName(roomName);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(roomService).getRoomDtoByRoomName(roomName);
        Mockito.verifyNoMoreInteractions(roomService);
    }
}
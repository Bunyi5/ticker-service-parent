package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreeningDetailsProvider {

    private final MovieService movieService;
    private final RoomService roomService;

    protected Movie getMovieByTitle(String title) {
        MovieDto movieDto = movieService.getMovieDtoByTitle(title);
        return convertDtoToMovieEntity(movieDto);
    }

    protected Room getRoomByRoomName(String roomName) {
        RoomDto roomDto = roomService.getRoomDtoByRoomName(roomName);
        return convertDtoToRoomEntity(roomDto);
    }

    private Movie convertDtoToMovieEntity(MovieDto movieDto) {
        return new Movie(movieDto.getId(),
                movieDto.getTitle(),
                movieDto.getGenre(),
                movieDto.getLength());
    }

    private Room convertDtoToRoomEntity(RoomDto roomDto) {
        return new Room(roomDto.getId(),
                roomDto.getRoomName(),
                roomDto.getRoomSeat(),
                roomDto.getRoomRow(),
                roomDto.getRoomColumn());
    }
}

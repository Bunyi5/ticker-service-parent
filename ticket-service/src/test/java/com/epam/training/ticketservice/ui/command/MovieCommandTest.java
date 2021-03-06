package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.ui.service.AvailabilityService;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.model.MovieDtoList;
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
public class MovieCommandTest {

    private MovieCommand movieCommand;

    @Mock
    private MovieService movieService;
    @Mock
    private AvailabilityService availabilityService;

    private static final String TITLE = "Sátántangó";
    private static final String GENRE = "drama";
    private static final int LENGTH = 450;
    private static final MovieDto MOVIE_DTO = MovieDto.builder()
            .title(TITLE)
            .genre(GENRE)
            .length(LENGTH)
            .build();

    @BeforeEach
    public void init() {
        movieCommand = new MovieCommand(movieService, availabilityService);
    }

    @Test
    public void testCreateMovieShouldCallMovieServiceWithTheRightParameters() {
        // Given
        Mockito.doNothing().when(movieService).createMovie(MOVIE_DTO);

        // When
        MovieDto actual = movieCommand.createMovie(TITLE, GENRE, LENGTH);

        // Then
        Assertions.assertEquals(MOVIE_DTO, actual);
        Mockito.verify(movieService).createMovie(MOVIE_DTO);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testUpdateMovieShouldCallMovieServiceWithTheRightParameters() {
        // Given
        Mockito.doNothing().when(movieService).updateMovie(MOVIE_DTO);

        // When
        MovieDto actual = movieCommand.updateMovie(TITLE, GENRE, LENGTH);

        // Then
        Assertions.assertEquals(MOVIE_DTO, actual);
        Mockito.verify(movieService).updateMovie(MOVIE_DTO);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testDeleteMovieShouldCallMovieServiceWithTheRightParameters() {
        // Given
        Mockito.doNothing().when(movieService).deleteMovie(TITLE);

        // When
        movieCommand.deleteMovie(TITLE);

        // Then
        Mockito.verify(movieService).deleteMovie(TITLE);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testListMoviesShouldReturnExpectedResult() {
        // Given
        MovieDtoList expected = new MovieDtoList(List.of(MOVIE_DTO));

        Mockito.when(movieService.getMovieDtoList())
                .thenReturn(expected);

        // When
        MovieDtoList actual = movieCommand.listMovies();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(movieService).getMovieDtoList();
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testIsSignedInAccountAdminShouldReturnExpected() {
        // Given
        Availability expected = Availability.available();

        Mockito.when(availabilityService.isSignedInAccountAdmin())
                .thenReturn(expected);

        // When
        Availability actual = movieCommand.isSignedInAccountAdmin();

        // Then
        Assertions.assertEquals(expected.isAvailable(), actual.isAvailable());
        Mockito.verify(availabilityService).isSignedInAccountAdmin();
        Mockito.verifyNoMoreInteractions(availabilityService);
    }
}
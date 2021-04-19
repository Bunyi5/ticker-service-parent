package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.persistence.entity.Movie;
import com.epam.training.ticketservice.core.service.AvailabilityService;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.core.service.model.MovieDto;
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

    private Movie movie;

    @BeforeEach
    public void init() {
        movieCommand = new MovieCommand(movieService, availabilityService);

        movie = new Movie();
        movie.setId(10L);
        movie.setTitle("Sátántangó");
        movie.setGenre("drama");
        movie.setMinutes(450);
    }

    @Test
    public void testCreateMovie() {
        // When
        movieCommand.createMovie(movie.getTitle(), movie.getGenre(), movie.getMinutes());

        // Then
        Mockito.verify(movieService).createMovie(movie.getTitle(), movie.getGenre(), movie.getMinutes());
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testUpdateMovie() {
        // When
        movieCommand.updateMovie(movie.getTitle(), movie.getGenre(), movie.getMinutes());

        // Then
        Mockito.verify(movieService).updateMovie(movie.getTitle(), movie.getGenre(), movie.getMinutes());
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testDeleteMovie() {
        // When
        movieCommand.deleteMovie(movie.getTitle());

        // Then
        Mockito.verify(movieService).deleteMovie(movie.getTitle());
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testListMoviesShouldReturnsExpected() {
        // Given
        MovieDto expected = new MovieDto();
        expected.setMovieList(List.of(movie));

        Mockito.when(movieService.getMovieDto())
                .thenReturn(expected);

        // When
        MovieDto actual = movieCommand.listMovies();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(movieService).getMovieDto();
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testIsSignedInAccountAdminShouldReturnsExpected() {
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
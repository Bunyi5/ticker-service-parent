package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.persistence.entity.Movie;
import com.epam.training.ticketservice.core.service.AvailabilityService;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.ui.command.model.MovieList;
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

    @BeforeEach
    public void init() {
        movieCommand = new MovieCommand(movieService, availabilityService);
    }

    @Test
    public void testCreateMovieShouldCallMovieServiceWithTheRightParameters() {
        // Given
        String title = "Sátántangó";
        String genre = "drama";
        int minutes = 450;

        // When
        movieCommand.createMovie(title, genre, minutes);

        // Then
        Mockito.verify(movieService).createMovie(title, genre, minutes);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testUpdateMovieShouldCallMovieServiceWithTheRightParameters() {
        // Given
        String title = "Sátántangó";
        String genre = "drama";
        int minutes = 450;

        // When
        movieCommand.updateMovie(title, genre, minutes);

        // Then
        Mockito.verify(movieService).updateMovie(title, genre, minutes);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testDeleteMovieShouldCallMovieServiceWithTheRightParameters() {
        // Given
        String title = "Sátántangó";

        // When
        movieCommand.deleteMovie(title);

        // Then
        Mockito.verify(movieService).deleteMovie(title);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void testListMoviesShouldReturnExpectedResult() {
        // Given
        Movie movie = new Movie(null, "Sátántangó", "drama", 450);
        MovieList expected = new MovieList(List.of(movie));

        Mockito.when(movieService.getMovieList())
                .thenReturn(expected);

        // When
        MovieList actual = movieCommand.listMovies();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(movieService).getMovieList();
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
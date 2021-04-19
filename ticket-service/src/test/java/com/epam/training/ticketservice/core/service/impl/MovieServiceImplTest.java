package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.MovieRepository;
import com.epam.training.ticketservice.core.persistence.entity.Movie;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.ui.command.model.MovieList;
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
public class MovieServiceImplTest {

    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    public void init() {
        movieService = new MovieServiceImpl(movieRepository);
    }

    @Test
    public void testCreateMovieShouldCallMovieRepositoryWithTheRightParameters() {
        // Given
        Movie expected = new Movie(null, "Sátántangó", "drama", 450);

        // When
        movieService.createMovie(expected.getTitle(), expected.getGenre(), expected.getMinutes());

        // Then
        Mockito.verify(movieRepository).save(expected);
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testUpdateMovieShouldCallMovieRepositoryWithTheRightParameters() {
        // Given
        Movie expected = new Movie(null, "Sátántangó", "thriller", 450);

        Mockito.when(movieRepository.findByTitle(expected.getTitle()))
                .thenReturn(Optional.of(new Movie()));

        // When
        movieService.updateMovie(expected.getTitle(), expected.getGenre(), expected.getMinutes());

        // Then
        Mockito.verify(movieRepository).findByTitle(expected.getTitle());
        Mockito.verify(movieRepository).save(expected);
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testUpdateMovieShouldThrowExceptionWhenMovieNotFound() {
        // Given
        String unknownMovieTitle = "notTitle";

        Mockito.when(movieRepository.findByTitle(unknownMovieTitle))
                .thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(NoSuchElementException.class,
                () -> movieService.updateMovie(unknownMovieTitle, "notGenre", 0));

        // Then
        Mockito.verify(movieRepository).findByTitle(unknownMovieTitle);
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testDeleteMovieShouldCallMovieRepository() {
        // When
        String title = "Sátántangó";

        movieService.deleteMovie(title);

        // Then
        Mockito.verify(movieRepository).deleteByTitle(title);
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testGetMovieDtoShouldReturnExpectedResultWhenTheMovieListContainsMovies() {
        // Given
        Movie movie = new Movie(null, "Sátántangó", "thriller", 450);
        MovieList expected = new MovieList(List.of(movie));

        Mockito.when(movieRepository.findAll())
                .thenReturn(List.of(movie));

        // When
        MovieList actual = movieService.getMovieList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(movieRepository).findAll();
        Mockito.verifyNoMoreInteractions(movieRepository);
    }
}
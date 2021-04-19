package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.MovieRepository;
import com.epam.training.ticketservice.core.persistence.entity.Movie;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.core.service.model.MovieDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;
    @Captor
    private ArgumentCaptor<Movie> movieCaptor;

    private Movie movie;

    @BeforeEach
    public void init() {
        movieService = new MovieServiceImpl(movieRepository);

        movie = new Movie();
        movie.setId(10L);
        movie.setTitle("Sátántangó");
        movie.setGenre("drama");
        movie.setMinutes(450);
    }

    @Test
    public void testCreateMovieShouldCreateMovieAndSaveIt() {
        // When
        movieService.createMovie(movie.getTitle(), movie.getGenre(), movie.getMinutes());

        // Then
        Mockito.verify(movieRepository).save(movieCaptor.capture());
        Mockito.verifyNoMoreInteractions(movieRepository);
        Assertions.assertEquals(movie.toString(), movieCaptor.getValue().toString());
    }

    @Test
    public void testUpdateMovieShouldUpdateMovieAndSaveIt() {
        // Given
        String genre = "thriller";
        Movie expected = new Movie(movie.getTitle(), genre, movie.getMinutes());

        Mockito.when(movieRepository.findByTitle(movie.getTitle()))
                .thenReturn(Optional.of(movie));

        // When
        movieService.updateMovie(movie.getTitle(), genre, movie.getMinutes());

        // Then
        Mockito.verify(movieRepository).findByTitle(movie.getTitle());
        Mockito.verify(movieRepository).save(movieCaptor.capture());
        Mockito.verifyNoMoreInteractions(movieRepository);
        Assertions.assertEquals(expected.toString(), movieCaptor.getValue().toString());
    }

    @Test
    public void testUpdateMovieShouldThrowExceptionWhenMovieNotFound() {
        // Given
        String unknownMovieTitle = "test";

        Mockito.when(movieRepository.findByTitle(unknownMovieTitle))
                .thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(NoSuchElementException.class,
                () -> movieService.updateMovie(unknownMovieTitle, movie.getGenre(), movie.getMinutes()));

        // Then
        Mockito.verify(movieRepository).findByTitle(unknownMovieTitle);
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testDeleteMovieShouldDeleteMovie() {
        // When
        movieService.deleteMovie(movie.getTitle());

        // Then
        Mockito.verify(movieRepository).deleteByTitle(movie.getTitle());
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testGetMovieDtoShouldReturnExpectedWhenTheMovieListNotContainsMovies() {
        // Given
        MovieDto expected = new MovieDto();
        expected.setMovieList(Collections.emptyList());

        Mockito.when(movieRepository.findAll())
                .thenReturn(Collections.emptyList());

        // When
        MovieDto actual = movieService.getMovieDto();

        // Then
        Assertions.assertEquals(expected.toString(), actual.toString());
        Mockito.verify(movieRepository).findAll();
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testGetMovieDtoShouldReturnExpectedWhenTheMovieListContainsMovies() {
        // Given
        MovieDto expected = new MovieDto();
        expected.setMovieList(List.of(movie));

        Mockito.when(movieRepository.findAll())
                .thenReturn(List.of(movie));

        // When
        MovieDto actual = movieService.getMovieDto();

        // Then
        Assertions.assertEquals(expected.toString(), actual.toString());
        Mockito.verify(movieRepository).findAll();
        Mockito.verifyNoMoreInteractions(movieRepository);
    }
}
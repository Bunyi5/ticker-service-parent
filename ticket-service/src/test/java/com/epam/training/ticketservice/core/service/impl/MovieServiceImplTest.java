package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.MovieRepository;
import com.epam.training.ticketservice.core.persistence.entity.Movie;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.core.service.model.MovieDto;
import com.epam.training.ticketservice.ui.command.model.MovieDtoList;
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

    private static final String TITLE = "Sátántangó";
    private static final String GENRE = "drama";
    private static final int MINUTES = 450;
    private static final MovieDto MOVIE_DTO = MovieDto.builder()
            .title(TITLE)
            .genre(GENRE)
            .minutes(MINUTES)
            .build();
    private static final Movie MOVIE_ENTITY = new Movie(null, TITLE, GENRE, MINUTES);

    @BeforeEach
    public void init() {
        movieService = new MovieServiceImpl(movieRepository);
    }

    @Test
    public void testCreateMovieShouldCallMovieRepositoryWhenTheInputMovieIsValid() {
        // Given
        Mockito.when(movieRepository.save(MOVIE_ENTITY))
                .thenReturn(MOVIE_ENTITY);

        // When
        movieService.createMovie(MOVIE_DTO);

        // Then
        Mockito.verify(movieRepository).save(MOVIE_ENTITY);
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenTheMovieIsNull() {
        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> movieService.createMovie(null));

        // Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenTheMovieTitleIsNull() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title(null)
                .genre(MOVIE_DTO.getGenre())
                .minutes(MOVIE_DTO.getMinutes())
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> movieService.createMovie(movieDto));

        // Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenTheMovieGenreIsNull() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title(MOVIE_DTO.getTitle())
                .genre(null)
                .minutes(MOVIE_DTO.getMinutes())
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> movieService.createMovie(movieDto));

        // Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenTheMovieMinutesIsZero() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title(MOVIE_DTO.getTitle())
                .genre(MOVIE_DTO.getGenre())
                .minutes(0)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> movieService.createMovie(movieDto));

        // Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testUpdateMovieShouldCallMovieRepositoryWhenTheInputMovieIsValid() {
        // Given
        Mockito.when(movieRepository.findByTitle(MOVIE_ENTITY.getTitle()))
                .thenReturn(Optional.of(MOVIE_ENTITY));

        // When
        movieService.updateMovie(MOVIE_DTO);

        // Then
        Mockito.verify(movieRepository).findByTitle(MOVIE_ENTITY.getTitle());
        Mockito.verify(movieRepository).save(MOVIE_ENTITY);
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testUpdateMovieShouldThrowExceptionWhenMovieNotFound() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title("notTitle")
                .genre(MOVIE_DTO.getGenre())
                .minutes(MOVIE_DTO.getMinutes())
                .build();

        Mockito.when(movieRepository.findByTitle(movieDto.getTitle()))
                .thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(NoSuchElementException.class,
                () -> movieService.updateMovie(movieDto));

        // Then
        Mockito.verify(movieRepository).findByTitle(movieDto.getTitle());
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testDeleteMovieShouldCallMovieRepositoryWhenTheInputTitleIsValid() {
        // Given
        String title = "Sátántangó";

        Mockito.doNothing().when(movieRepository).deleteByTitle(title);

        // When
        movieService.deleteMovie(title);

        // Then
        Mockito.verify(movieRepository).deleteByTitle(title);
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testGetMovieListShouldReturnExpectedResult() {
        // Given
        MovieDtoList expected = new MovieDtoList(List.of(MOVIE_DTO));

        Mockito.when(movieRepository.findAll())
                .thenReturn(List.of(MOVIE_ENTITY));

        // When
        MovieDtoList actual = movieService.getMovieDtoList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(movieRepository).findAll();
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testGetMovieDtoByTitleShouldReturnExpectedResult() {
        // Given
        Mockito.when(movieRepository.findByTitle(MOVIE_ENTITY.getTitle()))
                .thenReturn(Optional.of(MOVIE_ENTITY));

        // When
        MovieDto actual = movieService.getMovieDtoByTitle(MOVIE_ENTITY.getTitle());

        // Then
        Assertions.assertEquals(MOVIE_DTO, actual);
        Mockito.verify(movieRepository).findByTitle(MOVIE_ENTITY.getTitle());
        Mockito.verifyNoMoreInteractions(movieRepository);
    }
}
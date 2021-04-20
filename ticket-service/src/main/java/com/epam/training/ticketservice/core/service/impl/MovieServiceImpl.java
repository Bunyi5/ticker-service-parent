package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.MovieRepository;
import com.epam.training.ticketservice.core.persistence.entity.Movie;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.core.service.model.MovieDto;
import com.epam.training.ticketservice.ui.command.model.MovieDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public void createMovie(MovieDto movieDto) {
        saveMovie(null, movieDto);
    }

    @Override
    public void updateMovie(MovieDto movieDto) {
        Optional<Movie> maybeMovie = movieRepository.findByTitle(movieDto.getTitle());
        if (maybeMovie.isEmpty()) {
            throw new NoSuchElementException("Movie not found!");
        } else {
            saveMovie(maybeMovie.get().getId(), movieDto);
        }
    }

    @Override
    public void deleteMovie(String title) {
        Objects.requireNonNull(title, "Movie Title cannot be null");
        movieRepository.deleteByTitle(title);
    }

    @Override
    public MovieDtoList getMovieList() {
        return new MovieDtoList(getMovieDtoList());
    }

    private void saveMovie(Long id, MovieDto movieDto) {
        checkMovieDto(movieDto);
        Movie movie = new Movie(id,
                movieDto.getTitle(),
                movieDto.getGenre(),
                movieDto.getMinutes());
        movieRepository.save(movie);
    }

    private void checkMovieDto(MovieDto movieDto) {
        Objects.requireNonNull(movieDto, "Movie cannot be null");
        Objects.requireNonNull(movieDto.getTitle(), "Movie Title cannot be null");
        Objects.requireNonNull(movieDto.getGenre(), "Movie Genre cannot be null");
        if (movieDto.getMinutes() <= 0) {
            throw new NullPointerException("Movie Minutes cannot be zero or less");
        }
    }

    private List<MovieDto> getMovieDtoList() {
        return movieRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private MovieDto convertEntityToDto(Movie movie) {
        return MovieDto.builder()
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .minutes(movie.getMinutes())
                .build();
    }
}

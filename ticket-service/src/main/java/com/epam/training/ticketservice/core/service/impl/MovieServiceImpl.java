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
        Movie movie = getMovieByTitle(movieDto.getTitle());
        saveMovie(movie.getId(), movieDto);
    }

    @Override
    public void deleteMovie(String title) {
        Objects.requireNonNull(title, "Movie Title cannot be null");
        movieRepository.deleteByTitle(title);
    }

    @Override
    public MovieDtoList getMovieDtoList() {
        List<MovieDto> movieDtoList = movieRepository.findAll().stream()
                .map(this::convertEntityToDto).collect(Collectors.toList());
        return new MovieDtoList(movieDtoList);
    }

    @Override
    public MovieDto getMovieDtoByTitle(String title) {
        return convertEntityToDto(getMovieByTitle(title));
    }

    private void saveMovie(Long id, MovieDto movieDto) {
        checkNullInMovieDto(movieDto);
        Movie movie = new Movie(id,
                movieDto.getTitle(),
                movieDto.getGenre(),
                movieDto.getLength());
        movieRepository.save(movie);
    }

    private void checkNullInMovieDto(MovieDto movieDto) {
        Objects.requireNonNull(movieDto, "Movie cannot be null");
        Objects.requireNonNull(movieDto.getTitle(), "Movie Title cannot be null");
        Objects.requireNonNull(movieDto.getGenre(), "Movie Genre cannot be null");
        if (movieDto.getLength() <= 0) {
            throw new NullPointerException("Movie Length cannot be zero or less");
        }
    }

    private Movie getMovieByTitle(String title) {
        Optional<Movie> optionalMovie = movieRepository.findByTitle(title);

        if (optionalMovie.isEmpty()) {
            throw new NoSuchElementException("Movie not found!");
        }

        return optionalMovie.get();
    }

    private MovieDto convertEntityToDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .length(movie.getLength())
                .build();
    }
}

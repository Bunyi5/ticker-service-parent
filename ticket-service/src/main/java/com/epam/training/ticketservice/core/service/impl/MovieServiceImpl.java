package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.MovieRepository;
import com.epam.training.ticketservice.core.persistence.entity.Movie;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.ui.command.model.MovieList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public void createMovie(String title, String genre, int minutes) {
        Movie movie = new Movie(null, title, genre, minutes);
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(String title, String genre, int minutes) {
        Optional<Movie> maybeMovie = movieRepository.findByTitle(title);
        maybeMovie.ifPresentOrElse(
            (movie) -> {
                movie.setTitle(title);
                movie.setGenre(genre);
                movie.setMinutes(minutes);
                movieRepository.save(movie);
            }, () -> {
                throw new NoSuchElementException("Movie not found!");
            });
    }

    @Override
    public void deleteMovie(String title) {
        movieRepository.deleteByTitle(title);
    }

    @Override
    public MovieList getMovieList() {
        return new MovieList(movieRepository.findAll());
    }
}

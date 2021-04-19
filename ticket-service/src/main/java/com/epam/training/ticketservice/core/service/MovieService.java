package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.ui.command.model.MovieList;

public interface MovieService {

    void createMovie(String title, String genre, int minutes);

    void updateMovie(String title, String genre, int minutes);

    void deleteMovie(String title);

    MovieList getMovieList();
}

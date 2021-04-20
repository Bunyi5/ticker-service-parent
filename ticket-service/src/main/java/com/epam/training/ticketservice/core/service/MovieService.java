package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.core.service.model.MovieDto;
import com.epam.training.ticketservice.ui.command.model.MovieDtoList;

public interface MovieService {

    void createMovie(MovieDto movieDto);

    void updateMovie(MovieDto movieDto);

    void deleteMovie(String title);

    MovieDtoList getMovieList();
}

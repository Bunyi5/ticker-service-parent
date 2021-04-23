package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.model.MovieDtoList;

public interface MovieService {

    void createMovie(MovieDto movieDto);

    void updateMovie(MovieDto movieDto);

    void deleteMovie(String title);

    MovieDtoList getMovieDtoList();

    MovieDto getMovieDtoByTitle(String title);
}

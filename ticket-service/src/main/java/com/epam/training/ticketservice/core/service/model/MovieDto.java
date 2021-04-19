package com.epam.training.ticketservice.core.service.model;

import com.epam.training.ticketservice.core.persistence.entity.Movie;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Setter
public class MovieDto {

    List<Movie> movieList;

    @Override
    public String toString() {
        if (movieList.isEmpty()) {
            return "There are no movies at the moment";
        } else {
            return movieList.stream().map(Objects::toString).collect(Collectors.joining("\n"));
        }
    }
}

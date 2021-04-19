package com.epam.training.ticketservice.ui.command.model;

import com.epam.training.ticketservice.core.persistence.entity.Movie;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode
@RequiredArgsConstructor
public class MovieList {

    private final List<Movie> movieList;

    @Override
    public String toString() {
        if (movieList.isEmpty()) {
            return "There are no movies at the moment";
        } else {
            return movieList.stream().map(Objects::toString).collect(Collectors.joining("\n"));
        }
    }
}

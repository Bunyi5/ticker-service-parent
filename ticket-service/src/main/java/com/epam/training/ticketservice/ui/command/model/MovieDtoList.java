package com.epam.training.ticketservice.ui.command.model;

import com.epam.training.ticketservice.core.service.model.MovieDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class MovieDtoList {

    private final List<MovieDto> movieDtoList;

    @Override
    public String toString() {
        if (movieDtoList.isEmpty()) {
            return "There are no movies at the moment";
        } else {
            return movieDtoList.stream().map(Objects::toString).collect(Collectors.joining("\n"));
        }
    }
}

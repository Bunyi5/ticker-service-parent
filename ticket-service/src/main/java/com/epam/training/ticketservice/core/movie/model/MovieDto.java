package com.epam.training.ticketservice.core.movie.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MovieDto {

    private final Long id;
    private final String title;
    private final String genre;
    private final int length;

    @Override
    public String toString() {
        return title + " (" + genre + ", " + length + " minutes)";
    }
}

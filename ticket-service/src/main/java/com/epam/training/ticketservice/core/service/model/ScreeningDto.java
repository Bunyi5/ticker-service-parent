package com.epam.training.ticketservice.core.service.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScreeningDto {

    private final Long id;
    private final String movieTitle;
    private final String movieGenre;
    private final long movieMinutes;
    private final String roomName;
    private final LocalDate startDate;
    private final LocalTime startTime;

    @Override
    public String toString() {
        return movieTitle + " (" + movieGenre + ", " + movieMinutes
                + " minutes), screened in room " + roomName
                + ", at " + startDate.toString() + " " + startTime.toString();
    }
}

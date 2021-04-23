package com.epam.training.ticketservice.ui.command.model;

import com.epam.training.ticketservice.core.service.model.ScreeningDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ScreeningDtoList {

    private final List<ScreeningDto> screeningDtoList;

    @Override
    public String toString() {
        if (screeningDtoList.isEmpty()) {
            return "There are no screenings";
        } else {
            return screeningDtoList.stream().map(Objects::toString).collect(Collectors.joining("\n"));
        }
    }
}

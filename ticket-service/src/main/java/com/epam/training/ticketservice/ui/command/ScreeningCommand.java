package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.ui.service.AvailabilityService;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ShellComponent
@RequiredArgsConstructor
public class ScreeningCommand {

    private final ScreeningService screeningService;
    private final AvailabilityService availabilityService;

    @ShellMethod(value = "Create a screening", key = "create screening")
    public ScreeningDto createScreening(
            @ShellOption String movieTitle,
            @ShellOption String roomName,
            @ShellOption String startDateTime
    ) {
        ScreeningDto screeningDto = buildScreeningDto(movieTitle, roomName, startDateTime);
        return screeningService.createScreening(screeningDto);
    }

    @ShellMethod(value = "Delete a screening", key = "delete screening")
    public void deleteScreening(
            @ShellOption String movieTitle,
            @ShellOption String roomName,
            @ShellOption String startDateTime
    ) {
        ScreeningDto screeningDto = buildScreeningDto(movieTitle, roomName, startDateTime);
        screeningService.deleteScreening(screeningDto);
    }

    @ShellMethod(value = "List screenings", key = "list screenings")
    public ScreeningDtoList listScreenings() {
        return screeningService.getScreeningList();
    }

    @ShellMethodAvailability({"create screening", "delete screening"})
    public Availability isSignedInAccountAdmin() {
        return availabilityService.isSignedInAccountAdmin();
    }

    private ScreeningDto buildScreeningDto(String movieTitle, String roomName, String startDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(startDateTime, formatter);

        return ScreeningDto.builder()
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDate(localDateTime.toLocalDate())
                .startTime(localDateTime.toLocalTime())
                .build();
    }
}

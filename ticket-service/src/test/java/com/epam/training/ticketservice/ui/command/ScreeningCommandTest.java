package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.ui.service.AvailabilityService;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDtoList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.shell.Availability;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ScreeningCommandTest {

    private ScreeningCommand screeningCommand;

    @Mock
    private ScreeningService screeningService;
    @Mock
    private AvailabilityService availabilityService;

    private static final String MOVIE_TITLE = "Sátántangó";
    private static final String ROOM_NAME = "Pedersoli";
    private static final String START_DATE = "2021-10-22";
    private static final String START_TIME = "21:30";
    private static final ScreeningDto SCREENING_DTO = ScreeningDto.builder()
            .movieTitle(MOVIE_TITLE)
            .roomName(ROOM_NAME)
            .startDate(LocalDate.parse(START_DATE))
            .startTime(LocalTime.parse(START_TIME))
            .build();

    @BeforeEach
    public void init() {
        screeningCommand = new ScreeningCommand(screeningService, availabilityService);
    }

    @Test
    public void testCreateScreeningShouldCallScreeningServiceWithTheRightParameters() {
        // Given
        Mockito.when(screeningService.createScreening(SCREENING_DTO))
                .thenReturn(SCREENING_DTO);

        // When
        ScreeningDto actual = screeningCommand.createScreening(MOVIE_TITLE, ROOM_NAME,
                String.join(" ",START_DATE, START_TIME));

        // Then
        Assertions.assertEquals(SCREENING_DTO, actual);
        Mockito.verify(screeningService).createScreening(SCREENING_DTO);
        Mockito.verifyNoMoreInteractions(screeningService);
    }

    @Test
    public void testDeleteScreeningShouldCallScreeningServiceWithTheRightParameters() {
        // Given
        Mockito.doNothing().when(screeningService).deleteScreening(SCREENING_DTO);

        // When
        screeningCommand.deleteScreening(MOVIE_TITLE, ROOM_NAME,
                String.join(" ",START_DATE, START_TIME));

        // Then
        Mockito.verify(screeningService).deleteScreening(SCREENING_DTO);
        Mockito.verifyNoMoreInteractions(screeningService);
    }

    @Test
    public void testListScreeningsShouldReturnExpectedResult() {
        // Given
        ScreeningDtoList expected = new ScreeningDtoList(List.of(SCREENING_DTO));
        Mockito.when(screeningService.getScreeningList())
                .thenReturn(expected);

        // When
        ScreeningDtoList actual = screeningCommand.listScreenings();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(screeningService).getScreeningList();
        Mockito.verifyNoMoreInteractions(screeningService);
    }

    @Test
    public void testIsSignedInAccountAdminShouldReturnsExpected() {
        // Given
        Availability expected = Availability.available();

        Mockito.when(availabilityService.isSignedInAccountAdmin())
                .thenReturn(expected);

        // When
        Availability actual = screeningCommand.isSignedInAccountAdmin();

        // Then
        Assertions.assertEquals(expected.isAvailable(), actual.isAvailable());
        Mockito.verify(availabilityService).isSignedInAccountAdmin();
        Mockito.verifyNoMoreInteractions(availabilityService);
    }
}
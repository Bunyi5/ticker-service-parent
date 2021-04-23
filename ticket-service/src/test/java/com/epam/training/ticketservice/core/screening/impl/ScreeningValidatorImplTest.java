package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.ScreeningValidator;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ScreeningValidatorImplTest {

    private ScreeningValidator screeningValidator;

    @Mock
    private ScreeningRepository screeningRepository;

    private static final Movie MOVIE_ENTITY = new Movie(
            10L, "Sátántangó", "drama", 120
    );
    private static final Room ROOM_ENTITY = new Room(
            20L, "Pedersoli", 20 * 10, 20, 10
    );

    private static final LocalDate START_DATE = LocalDate.of(2021, 1, 1);
    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final Screening SCREENING_ENTITY = new Screening(
            null, MOVIE_ENTITY, ROOM_ENTITY, START_DATE, START_TIME
    );

    @BeforeEach
    public void init() {
        screeningValidator = new ScreeningValidatorImpl(screeningRepository);
    }

    @Test
    public void testCheckNullInScreeningDtoShouldRunSuccessfully() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("Sátántangó")
                .roomName("Pedersoli")
                .startDate(LocalDate.of(2021, 10, 22))
                .startTime(LocalTime.of(21, 30))
                .build();

        // When
        screeningValidator.checkNullInScreeningDto(screeningDto);
    }

    @Test
    public void testCheckNullInScreeningDtoShouldThrowNullPointerExceptionWhenScreeningIsNull() {
        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> screeningValidator.checkNullInScreeningDto(null));
    }

    @Test
    public void testCheckNullInScreeningDtoShouldThrowNullPointerExceptionWhenScreeningMovieTitleIsNull() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle(null)
                .roomName("Pedersoli")
                .startDate(LocalDate.of(2021, 10, 22))
                .startTime(LocalTime.of(21, 30))
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> screeningValidator.checkNullInScreeningDto(screeningDto));
    }

    @Test
    public void testCheckNullInScreeningDtoShouldThrowNullPointerExceptionWhenScreeningRoomNameIsNull() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("Sátántangó")
                .roomName(null)
                .startDate(LocalDate.of(2021, 10, 22))
                .startTime(LocalTime.of(21, 30))
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> screeningValidator.checkNullInScreeningDto(screeningDto));
    }

    @Test
    public void testCheckNullInScreeningDtoShouldThrowNullPointerExceptionWhenScreeningStartDateIsNull() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("Sátántangó")
                .roomName("Pedersoli")
                .startDate(null)
                .startTime(LocalTime.of(21, 30))
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> screeningValidator.checkNullInScreeningDto(screeningDto));
    }

    @Test
    public void testCheckNullInScreeningDtoShouldThrowNullPointerExceptionWhenScreeningStartTimeIsNull() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("Sátántangó")
                .roomName("Pedersoli")
                .startDate(LocalDate.of(2021, 10, 22))
                .startTime(null)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class,
                () -> screeningValidator.checkNullInScreeningDto(screeningDto));
    }


    @ParameterizedTest
    @CsvSource({"13,5", "9,5"})
    public void testValidateScreeningShouldRunSuccessfullyWhenScreeningTimesNotOverlap(int startTime, int movieLength) {
        // Given
        Movie movie = new Movie(5L, "Spirited Away", "animation", movieLength);

        Screening screening = new Screening(
                null, movie, ROOM_ENTITY,
                LocalDate.of(2021, 1, 1), LocalTime.of(startTime, 0)
        );

        Mockito.when(screeningRepository.findAllByRoomRoomNameAndStartDate(
                screening.getRoom().getRoomName(),
                screening.getStartDate()
        )).thenReturn(List.of(SCREENING_ENTITY));

        // When
        screeningValidator.validateScreening(screening);

        // Then
        Mockito.verify(screeningRepository).findAllByRoomRoomNameAndStartDate(
                screening.getRoom().getRoomName(),
                screening.getStartDate()
        );
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @ParameterizedTest
    @CsvSource({"9,960", "9,120"})
    public void testValidateScreeningShouldThrowExceptionWhenScreeningTimesOverlap(int startTime, int movieLength) {
        // Given
        Movie movie = new Movie(5L, "Spirited Away", "animation", movieLength);

        Screening screening = new Screening(
                null, movie, ROOM_ENTITY,
                LocalDate.of(2021, 1, 1), LocalTime.of(startTime, 0)
        );

        Mockito.when(screeningRepository.findAllByRoomRoomNameAndStartDate(
                screening.getRoom().getRoomName(),
                screening.getStartDate()
        )).thenReturn(List.of(SCREENING_ENTITY));

        // When
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> screeningValidator.validateScreening(screening));

        // Then
        Mockito.verify(screeningRepository).findAllByRoomRoomNameAndStartDate(
                screening.getRoom().getRoomName(),
                screening.getStartDate()
        );
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @ParameterizedTest
    @CsvSource({"13,5", "9,5"})
    public void testValidateScreeningShouldRunSuccessfullyWhenScreeningBreakTimesNotOverlap(
            int startTime, int movieLength
    ) {
        // Given
        Movie movie = new Movie(5L, "Spirited Away", "animation", movieLength);

        Screening screening = new Screening(
                null, movie, ROOM_ENTITY,
                LocalDate.of(2021, 1, 1), LocalTime.of(startTime, 0)
        );

        Mockito.when(screeningRepository.findAllByRoomRoomNameAndStartDate(
                screening.getRoom().getRoomName(),
                screening.getStartDate()
        )).thenReturn(List.of(SCREENING_ENTITY));

        // When
        screeningValidator.validateScreening(screening);

        // Then
        Mockito.verify(screeningRepository).findAllByRoomRoomNameAndStartDate(
                screening.getRoom().getRoomName(),
                screening.getStartDate()
        );
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @ParameterizedTest
    @CsvSource({"12,5", "9,51"})
    public void testValidateScreeningShouldThrowExceptionWhenScreeningBreakTimesOverlap(
            int startTime, int movieLength
    ) {
        // Given
        Movie movie = new Movie(5L, "Spirited Away", "animation", movieLength);

        Screening screening = new Screening(
                null, movie, ROOM_ENTITY,
                LocalDate.of(2021, 1, 1), LocalTime.of(startTime, 1)
        );

        Mockito.when(screeningRepository.findAllByRoomRoomNameAndStartDate(
                screening.getRoom().getRoomName(),
                screening.getStartDate()
        )).thenReturn(List.of(SCREENING_ENTITY));

        // When
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> screeningValidator.validateScreening(screening));

        // Then
        Mockito.verify(screeningRepository).findAllByRoomRoomNameAndStartDate(
                screening.getRoom().getRoomName(),
                screening.getStartDate()
        );
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }
}
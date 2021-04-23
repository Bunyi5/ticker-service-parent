package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.ScreeningRepository;
import com.epam.training.ticketservice.core.persistence.entity.Movie;
import com.epam.training.ticketservice.core.persistence.entity.Room;
import com.epam.training.ticketservice.core.persistence.entity.Screening;
import com.epam.training.ticketservice.core.service.ScreeningService;
import com.epam.training.ticketservice.core.service.ScreeningValidator;
import com.epam.training.ticketservice.core.service.model.ScreeningDto;
import com.epam.training.ticketservice.ui.command.model.ScreeningDtoList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ScreeningServiceImplTest {

    private ScreeningService screeningService;

    @Mock
    private ScreeningValidator screeningValidator;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private ScreeningDetailsProvider screeningDetailsProvider;

    private static final Movie MOVIE_ENTITY = new Movie(
            10L, "Sátántangó", "drama", 450
    );
    private static final Room ROOM_ENTITY = new Room(
            20L, "Pedersoli", 20 * 10, 20, 10
    );

    private static final LocalDate START_DATE = LocalDate.of(2021, 10, 22);
    private static final LocalTime START_TIME = LocalTime.of(21, 30);
    private static final Screening SCREENING_ENTITY = new Screening(
            null, MOVIE_ENTITY, ROOM_ENTITY, START_DATE, START_TIME
    );
    private static final ScreeningDto SCREENING_DTO = ScreeningDto.builder()
            .movieTitle(MOVIE_ENTITY.getTitle())
            .movieGenre(MOVIE_ENTITY.getGenre())
            .movieLength(MOVIE_ENTITY.getLength())
            .roomName(ROOM_ENTITY.getRoomName())
            .startDate(START_DATE)
            .startTime(START_TIME)
            .build();

    @BeforeEach
    public void init() {
        screeningService = new ScreeningServiceImpl(screeningValidator, screeningRepository, screeningDetailsProvider);
    }

    @Test
    public void testCreateScreeningShouldCallProviderAndValidatorAndRepositoryAndReturnExpected() {
        // Given
        Mockito.doNothing().when(screeningValidator).checkNullInScreeningDto(SCREENING_DTO);
        Mockito.when(screeningDetailsProvider.getMovieByTitle(MOVIE_ENTITY.getTitle()))
                .thenReturn(MOVIE_ENTITY);
        Mockito.when(screeningDetailsProvider.getRoomByRoomName(ROOM_ENTITY.getRoomName()))
                .thenReturn(ROOM_ENTITY);
        Mockito.doNothing().when(screeningValidator).validateScreening(SCREENING_ENTITY);
        Mockito.when(screeningRepository.save(SCREENING_ENTITY))
                .thenReturn(SCREENING_ENTITY);

        // When
        ScreeningDto actual = screeningService.createScreening(SCREENING_DTO);

        // Then
        Assertions.assertEquals(SCREENING_DTO, actual);
        Mockito.verify(screeningValidator).checkNullInScreeningDto(SCREENING_DTO);
        Mockito.verify(screeningDetailsProvider).getMovieByTitle(MOVIE_ENTITY.getTitle());
        Mockito.verify(screeningDetailsProvider).getRoomByRoomName(ROOM_ENTITY.getRoomName());
        Mockito.verify(screeningValidator).validateScreening(SCREENING_ENTITY);
        Mockito.verify(screeningRepository).save(SCREENING_ENTITY);
        Mockito.verifyNoMoreInteractions(screeningValidator, screeningDetailsProvider, screeningRepository);
    }

    @Test
    public void testDeleteScreeningShouldCallScreeningRepositoryWithTheRightParameters() {
        // Given
        Mockito.doNothing().when(screeningRepository).deleteByMovieTitleAndRoomRoomNameAndStartDateAndStartTime(
                SCREENING_DTO.getMovieTitle(),
                SCREENING_DTO.getRoomName(),
                SCREENING_DTO.getStartDate(),
                SCREENING_DTO.getStartTime()
        );

        // When
        screeningService.deleteScreening(SCREENING_DTO);

        // Then
        Mockito.verify(screeningRepository).deleteByMovieTitleAndRoomRoomNameAndStartDateAndStartTime(
                SCREENING_DTO.getMovieTitle(),
                SCREENING_DTO.getRoomName(),
                SCREENING_DTO.getStartDate(),
                SCREENING_DTO.getStartTime()
        );
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testGetScreeningListShouldCallScreeningRepositoryAndReturnExpected() {
        // Given
        ScreeningDtoList expected = new ScreeningDtoList(List.of(SCREENING_DTO));

        Mockito.when(screeningRepository.findAll())
                .thenReturn(List.of(SCREENING_ENTITY));

        // When
        ScreeningDtoList actual = screeningService.getScreeningList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(screeningRepository).findAll();
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }
}
package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.ScreeningRepository;
import com.epam.training.ticketservice.core.persistence.entity.Movie;
import com.epam.training.ticketservice.core.persistence.entity.Room;
import com.epam.training.ticketservice.core.persistence.entity.Screening;
import com.epam.training.ticketservice.core.service.ScreeningService;
import com.epam.training.ticketservice.core.service.ScreeningValidator;
import com.epam.training.ticketservice.core.service.model.ScreeningDto;
import com.epam.training.ticketservice.ui.command.model.ScreeningDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningValidator screeningValidator;
    private final ScreeningRepository screeningRepository;
    private final ScreeningDetailsProvider screeningDetailsProvider;

    @Override
    public ScreeningDto createScreening(ScreeningDto screeningDto) {
        screeningValidator.checkNullInScreeningDto(screeningDto);
        return saveScreening(screeningDto);
    }

    @Override
    public void deleteScreening(ScreeningDto screeningDto) {
        screeningRepository.deleteByMovieTitleAndRoomRoomNameAndStartDateAndStartTime(
                screeningDto.getMovieTitle(),
                screeningDto.getRoomName(),
                screeningDto.getStartDate(),
                screeningDto.getStartTime()
        );
    }

    @Override
    public ScreeningDtoList getScreeningList() {
        List<ScreeningDto> screeningDtoList = screeningRepository.findAll()
                .stream().map(this::convertEntityToDto).collect(Collectors.toList());
        return new ScreeningDtoList(screeningDtoList);
    }

    private ScreeningDto saveScreening(ScreeningDto screeningDto) {

        Movie movie = screeningDetailsProvider.getMovieByTitle(screeningDto.getMovieTitle());
        Room room = screeningDetailsProvider.getRoomByRoomName(screeningDto.getRoomName());

        Screening screening = createScreeningEntity(movie, room, screeningDto);

        screeningValidator.validateScreening(screening);
        screeningRepository.save(screening);
        return convertEntityToDto(screening);
    }

    private Screening createScreeningEntity(Movie movie, Room room, ScreeningDto screeningDto) {
        return new Screening(null,
                movie,
                room,
                screeningDto.getStartDate(),
                screeningDto.getStartTime());
    }

    private ScreeningDto convertEntityToDto(Screening screening) {
        return ScreeningDto.builder()
                .id(screening.getId())
                .movieTitle(screening.getMovie().getTitle())
                .movieGenre(screening.getMovie().getGenre())
                .movieMinutes(screening.getMovie().getMinutes())
                .roomName(screening.getRoom().getRoomName())
                .startDate(screening.getStartDate())
                .startTime(screening.getStartTime())
                .build();
    }
}
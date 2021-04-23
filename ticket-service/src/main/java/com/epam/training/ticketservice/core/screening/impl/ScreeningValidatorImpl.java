package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.ScreeningValidator;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScreeningValidatorImpl implements ScreeningValidator {

    private final ScreeningRepository screeningRepository;

    @Override
    public void checkNullInScreeningDto(ScreeningDto screeningDto) {
        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getMovieTitle(), "Screening Movie Title cannot be null");
        Objects.requireNonNull(screeningDto.getRoomName(), "Screening Room Name cannot be null");
        Objects.requireNonNull(screeningDto.getStartDate(), "Screening Start Date cannot be null");
        Objects.requireNonNull(screeningDto.getStartTime(), "Screening Start Time cannot be null");
    }

    @Override
    public void validateScreening(Screening newScreening) {
        List<Screening> screeningList =
                screeningRepository.findAllByRoomRoomNameAndStartDate(
                        newScreening.getRoom().getRoomName(),
                        newScreening.getStartDate()
                );

        screeningList.forEach(existingScreening -> validateScreeningTime(newScreening, existingScreening));
    }

    private void validateScreeningTime(Screening newScreening, Screening existingScreening) {
        int lengthOfNew = newScreening.getMovie().getLength();
        int lengthOfExisting = existingScreening.getMovie().getLength();

        LocalTime startTimeOfNew = newScreening.getStartTime();
        LocalTime startTimeOfExisting = existingScreening.getStartTime();
        LocalTime endTimeOfNew = startTimeOfNew.plus(lengthOfNew, ChronoUnit.MINUTES);
        LocalTime endTimeOfExisting = startTimeOfExisting.plus(lengthOfExisting, ChronoUnit.MINUTES);

        checkOverlapping(startTimeOfNew, startTimeOfExisting, endTimeOfNew, endTimeOfExisting);
        checkIsScreeningInTheBreak(startTimeOfNew, startTimeOfExisting, endTimeOfNew, endTimeOfExisting);
    }

    private void checkOverlapping(LocalTime startTimeOfNew, LocalTime startTimeOfExisting,
                                  LocalTime endTimeOfNew, LocalTime endTimeOfExisting) {

        if (isScreeningTimesOverlap(startTimeOfNew, startTimeOfExisting, endTimeOfNew, endTimeOfExisting)) {
            System.out.println("There is an overlapping screening");
            throw new IllegalArgumentException("There is an overlapping screening");
        }
    }

    private void checkIsScreeningInTheBreak(LocalTime startTimeOfNew, LocalTime startTimeOfExisting,
                                            LocalTime endTimeOfNew, LocalTime endTimeOfExisting) {

        if (isScreeningInTheBreak(startTimeOfNew, startTimeOfExisting, endTimeOfNew, endTimeOfExisting)) {
            System.out.println("This would start in the break period after another screening in this room");
            throw new IllegalArgumentException(
                    "This would start in the break period after another screening in this room"
            );
        }
    }

    private boolean isScreeningTimesOverlap(LocalTime startTimeOfNew, LocalTime startTimeOfExisting,
                                            LocalTime endTimeOfNew, LocalTime endTimeOfExisting) {

        return !startTimeOfNew.isAfter(endTimeOfExisting)
                && !(endTimeOfNew.isBefore(startTimeOfExisting) && endTimeOfNew.isAfter(startTimeOfNew));
    }

    private boolean isScreeningInTheBreak(LocalTime startTimeOfNew, LocalTime startTimeOfExisting,
                                          LocalTime endTimeOfNew, LocalTime endTimeOfExisting) {

        LocalTime endBreakTimeOfNew = endTimeOfNew.plus(10, ChronoUnit.MINUTES);
        LocalTime endBreakTimeOfExisting = endTimeOfExisting.plus(10, ChronoUnit.MINUTES);

        return (!startTimeOfNew.isAfter(endBreakTimeOfExisting) && startTimeOfNew.isAfter(endTimeOfExisting))
                || (!startTimeOfExisting.isAfter(endBreakTimeOfNew) && startTimeOfExisting.isAfter(endTimeOfNew));
    }
}

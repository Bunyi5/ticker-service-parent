package com.epam.training.ticketservice.core.persistence;

import com.epam.training.ticketservice.core.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findAllByRoomRoomNameAndStartDate(String roomName, LocalDate startDate);

    @Transactional
    void deleteByMovieTitleAndRoomRoomNameAndStartDateAndStartTime(
            String movieTitle,
            String roomName,
            LocalDate startDate,
            LocalTime startTime
    );
}

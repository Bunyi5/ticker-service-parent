package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDtoList;

public interface ScreeningService {

    ScreeningDto createScreening(ScreeningDto screeningDto);

    void deleteScreening(ScreeningDto screeningDto);

    ScreeningDtoList getScreeningList();
}

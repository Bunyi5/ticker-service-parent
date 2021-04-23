package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.core.service.model.ScreeningDto;
import com.epam.training.ticketservice.ui.command.model.ScreeningDtoList;

public interface ScreeningService {

    ScreeningDto createScreening(ScreeningDto screeningDto);

    void deleteScreening(ScreeningDto screeningDto);

    ScreeningDtoList getScreeningList();
}

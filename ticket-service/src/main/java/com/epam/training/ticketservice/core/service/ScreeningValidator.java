package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.core.persistence.entity.Screening;
import com.epam.training.ticketservice.core.service.model.ScreeningDto;

public interface ScreeningValidator {

    void checkNullInScreeningDto(ScreeningDto screeningDto);

    void validateScreening(Screening screening);
}

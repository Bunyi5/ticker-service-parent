package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

public interface ScreeningValidator {

    void checkNullInScreeningDto(ScreeningDto screeningDto);

    void validateScreening(Screening screening);
}

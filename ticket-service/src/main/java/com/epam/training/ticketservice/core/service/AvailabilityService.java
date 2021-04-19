package com.epam.training.ticketservice.core.service;

import org.springframework.shell.Availability;

public interface AvailabilityService {

    Availability isThereASignedInAccount();
}

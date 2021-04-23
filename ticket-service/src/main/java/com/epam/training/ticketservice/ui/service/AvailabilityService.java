package com.epam.training.ticketservice.ui.service;

import org.springframework.shell.Availability;

public interface AvailabilityService {

    Availability isThereASignedInAccount();

    Availability isSignedInAccountAdmin();
}

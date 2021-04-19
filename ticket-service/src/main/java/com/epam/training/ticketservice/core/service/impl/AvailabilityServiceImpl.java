package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.service.AuthenticationService;
import com.epam.training.ticketservice.core.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.shell.Availability;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AuthenticationService authenticationService;

    public Availability isThereASignedInAccount() {
        try {
            authenticationService.getSignedInAccount();
            return Availability.available();
        } catch (AuthenticationCredentialsNotFoundException e) {
            return Availability.unavailable("You are not signed in");
        }
    }
}

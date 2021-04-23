package com.epam.training.ticketservice.ui.service.impl;

import com.epam.training.ticketservice.core.account.AuthenticationService;
import com.epam.training.ticketservice.ui.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.shell.Availability;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AuthenticationService authenticationService;

    @Override
    public Availability isThereASignedInAccount() {
        try {
            authenticationService.getSignedInAccount();
            return Availability.available();
        } catch (AuthenticationCredentialsNotFoundException e) {
            return Availability.unavailable("You are not signed in");
        }
    }

    @Override
    public Availability isSignedInAccountAdmin() {
        return authenticationService.isSignedInAccountAdmin()
                ? Availability.available()
                : Availability.unavailable("You are not an admin");
    }
}

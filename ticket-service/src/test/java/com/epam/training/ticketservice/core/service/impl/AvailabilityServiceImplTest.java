package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.entity.Account;
import com.epam.training.ticketservice.core.service.AuthenticationService;
import com.epam.training.ticketservice.core.service.AvailabilityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.shell.Availability;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceImplTest {

    private AvailabilityService availabilityService;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    public void init() {
        availabilityService = new AvailabilityServiceImpl(authenticationService);
    }

    @Test
    public void testIsThereASignedInAccountShouldReturnAvailable() {
        // Given
        Availability expected = Availability.available();

        Mockito.when(authenticationService.getSignedInAccount())
                .thenReturn(new Account());

        // When
        Availability actual = availabilityService.isThereASignedInAccount();

        // Then
        Assertions.assertEquals(expected.isAvailable(), actual.isAvailable());
        Mockito.verify(authenticationService).getSignedInAccount();
        Mockito.verifyNoMoreInteractions(authenticationService);
    }

    @Test
    public void testIsThereASignedInAccountShouldReturnUnAvailable() {
        // Given
        Availability expected = Availability.unavailable("You are not signed in");

        Mockito.when(authenticationService.getSignedInAccount())
                .thenThrow(AuthenticationCredentialsNotFoundException.class);

        // When
        Availability actual = availabilityService.isThereASignedInAccount();

        // Then
        Assertions.assertEquals(expected.isAvailable(), actual.isAvailable());
        Mockito.verify(authenticationService).getSignedInAccount();
        Mockito.verifyNoMoreInteractions(authenticationService);
    }
}
package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.service.AvailabilityService;
import com.epam.training.ticketservice.core.service.LoginService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.shell.Availability;

@ExtendWith(MockitoExtension.class)
public class LoginCommandTest {

    private LoginCommand loginCommand;

    @Mock
    private LoginService loginService;
    @Mock
    private AvailabilityService availabilityService;

    @BeforeEach
    public void init() {
        loginCommand = new LoginCommand(loginService, availabilityService);
    }

    @Test
    public void testSignInPrivilegedShouldReturnNullWhenTheRightCredentialsAreUsed() {
        // Given
        String adminUsername = "admin";
        String adminPassword = "admin";

        // When
        String actual = loginCommand.signInPrivileged(adminUsername, adminPassword);

        // Then
        Assertions.assertNull(actual);
        Mockito.verify(loginService).signInPrivileged(adminUsername, adminPassword);
        Mockito.verifyNoMoreInteractions(loginService);
    }

    @Test
    public void testSignInPrivilegedShouldReturnExpectedResultWhenTheWrongCredentialsAreUsed() {
        // Given
        String adminUsername = "admin";
        String wrongPassword = "notAdmin";

        String expected = "Login failed due to incorrect credentials";

        Mockito.doThrow(BadCredentialsException.class).when(loginService)
                .signInPrivileged(adminUsername, wrongPassword);

        // When
        String actual = loginCommand.signInPrivileged(adminUsername, wrongPassword);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(loginService).signInPrivileged(adminUsername, wrongPassword);
        Mockito.verifyNoMoreInteractions(loginService);
    }

    @Test
    public void testSignOutShouldCallLoginService() {
        // When
        loginCommand.signOut();

        // Then
        Mockito.verify(loginService).signOut();
        Mockito.verifyNoMoreInteractions(loginService);
    }

    @Test
    public void testDescribeAccountShouldReturnExpected() {
        // Given
        String expected = "Signed in with privileged account 'admin'";

        Mockito.when(loginService.describeAccount())
                .thenReturn(expected);

        // When
        String actual = loginCommand.describeAccount();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(loginService).describeAccount();
        Mockito.verifyNoMoreInteractions(loginService);
    }

    @Test
    public void testIsThereASignedInAccountShouldReturnExpected() {
        // Given
        Availability expected = Availability.available();

        Mockito.when(availabilityService.isThereASignedInAccount())
                .thenReturn(expected);

        // When
        Availability actual = loginCommand.isThereASignedInAccount();

        // Then
        Assertions.assertEquals(expected.isAvailable(), actual.isAvailable());
        Mockito.verify(availabilityService).isThereASignedInAccount();
        Mockito.verifyNoMoreInteractions(availabilityService);
    }
}
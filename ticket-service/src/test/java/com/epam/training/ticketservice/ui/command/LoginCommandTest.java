package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.persistence.entity.Account;
import com.epam.training.ticketservice.core.service.AvailabilityService;
import com.epam.training.ticketservice.core.service.impl.LoginServiceImpl;
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
    private LoginServiceImpl loginService;
    @Mock
    private AvailabilityService availabilityService;

    private Account adminAccount;

    @BeforeEach
    public void init() {
        loginCommand = new LoginCommand(loginService, availabilityService);

        adminAccount = new Account();
        adminAccount.setId(200L);
        adminAccount.setUsername("admin");
        adminAccount.setPassword("admin");
    }

    @Test
    public void testSignInPrivilegedShouldReturnsExpectedWhenTheRightCredentialsAreUsed() {
        // When
        String actual = loginCommand.signInPrivileged(adminAccount.getUsername(), adminAccount.getPassword());

        // Then
        Assertions.assertNull(actual);
        Mockito.verify(loginService).signInPrivileged(adminAccount.getUsername(), adminAccount.getPassword());
        Mockito.verifyNoMoreInteractions(loginService);
    }

    @Test
    public void testSignInPrivilegedShouldReturnsExpectedWhenTheWrongCredentialsAreUsed() {
        // Given
        String wrongPassword = "notAdmin";
        String expected = "Login failed due to incorrect credentials";

        Mockito.doThrow(BadCredentialsException.class).when(loginService)
                .signInPrivileged(adminAccount.getUsername(), wrongPassword);

        // When
        String actual = loginCommand.signInPrivileged(adminAccount.getUsername(), wrongPassword);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(loginService).signInPrivileged(adminAccount.getUsername(), wrongPassword);
        Mockito.verifyNoMoreInteractions(loginService);
    }

    @Test
    public void testSignOut() {
        // When
        loginCommand.signOut();

        // Then
        Mockito.verify(loginService).signOut();
        Mockito.verifyNoMoreInteractions(loginService);
    }

    @Test
    public void testDescribeAccountShouldReturnsExpected() {
        // Given
        String expected = "Signed in with privileged account '" + adminAccount.getUsername() + "'";

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
    public void testIsThereASignedInAccountShouldReturnsExpected() {
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
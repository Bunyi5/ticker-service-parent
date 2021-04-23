package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.entity.Account;
import com.epam.training.ticketservice.core.service.AuthenticationService;
import com.epam.training.ticketservice.core.service.LoginService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

    private LoginService loginService;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    public void init() {
        loginService = new LoginServiceImpl(authenticationService);
    }

    @Test
    public void testSignInPrivilegedShouldCallAuthenticationServiceWithTheRightParameters() {
        // Given
        String username = "admin";
        String password = "admin";

        Mockito.doNothing().when(authenticationService).signIn(username, password);

        // When
        loginService.signInPrivileged(username, password);

        // Then
        Mockito.verify(authenticationService).signIn(username, password);
        Mockito.verifyNoMoreInteractions(authenticationService);
    }

    @Test
    public void testSignOutShouldCallAuthenticationService() {
        // When
        loginService.signOut();

        // Then
        Mockito.verify(authenticationService).clearAuthentication();
        Mockito.verifyNoMoreInteractions(authenticationService);
    }

    @Test
    public void testDescribeAccountShouldReturnExpectedResultWhenAdminIsSignedIn() {
        // Given
        Account adminAccount = new Account(200L, "admin",
                "$2y$04$LAI2hWUb1WB7hlnSfHCAEuqkybgnr7RKLJrBIi5m4gp6OOUEwCvmi", true);

        String expected = "Signed in with privileged account '" + adminAccount.getUsername() + "'";

        Mockito.when(authenticationService.getSignedInAccount())
                .thenReturn(adminAccount);

        // When
        String actual = loginService.describeAccount();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(authenticationService).getSignedInAccount();
        Mockito.verifyNoMoreInteractions(authenticationService);
    }

    @Test
    public void testDescribeAccountShouldReturnExpectedResultWhenUserIsSignedIn() {
        // Given
        Account userAccount = new Account(100L, "test",
                "$2y$04$4xBBVeTKzpGQfnSnaY7CB.rYzcOAcX62f3mjNqKmlE/29sCx0x5wC", false);

        String expected = "Signed in with account '" + userAccount.getUsername() + "'";

        Mockito.when(authenticationService.getSignedInAccount())
                .thenReturn(userAccount);

        // When
        String actual = loginService.describeAccount();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(authenticationService).getSignedInAccount();
        Mockito.verifyNoMoreInteractions(authenticationService);
    }

    @Test
    public void testDescribeAccountShouldReturnsExpectedResultWhenNoBodyIsSignedIn() {
        // Given
        String expected = "You are not signed in";

        Mockito.when(authenticationService.getSignedInAccount())
                .thenThrow(AuthenticationCredentialsNotFoundException.class);

        // When
        String actual = loginService.describeAccount();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(authenticationService).getSignedInAccount();
        Mockito.verifyNoMoreInteractions(authenticationService);
    }
}

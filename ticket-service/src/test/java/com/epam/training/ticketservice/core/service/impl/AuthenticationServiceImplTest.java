package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.entity.Account;
import com.epam.training.ticketservice.core.service.AuthenticationService;
import com.epam.training.ticketservice.core.service.model.AccountDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    private AuthenticationService authenticationService;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void init() {
        authenticationService = new AuthenticationServiceImpl();
    }

    @Test
    public void testSetAuthenticationShouldCallSecurityContextWithTheRightParameters() {
        // Given
        Account adminAccount = new Account(200L, "admin",
                "$2y$04$LAI2hWUb1WB7hlnSfHCAEuqkybgnr7RKLJrBIi5m4gp6OOUEwCvmi", true);
        UserDetails adminAccountDetails = new AccountDetails(adminAccount);
        UsernamePasswordAuthenticationToken adminUsernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(adminAccountDetails, null, null);

        SecurityContextHolder.setContext(securityContext);

        // When
        authenticationService.setAuthentication(adminAccountDetails);

        // Then
        Mockito.verify(securityContext).setAuthentication(adminUsernamePasswordAuthenticationToken);
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testClearAuthenticationShouldCallSecurityContextWithTheRightParameters() {
        // Given
        SecurityContextHolder.setContext(securityContext);

        // When
        authenticationService.clearAuthentication();

        // Then
        Mockito.verify(securityContext).setAuthentication(null);
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testGetSignedInAccountShouldReturnExpectedResultWhenAdminIsSignedIn() {
        // Given
        Account adminAccount = new Account(200L, "admin",
                "$2y$04$LAI2hWUb1WB7hlnSfHCAEuqkybgnr7RKLJrBIi5m4gp6OOUEwCvmi", true);
        UserDetails adminAccountDetails = new AccountDetails(adminAccount);
        UsernamePasswordAuthenticationToken adminUsernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(adminAccountDetails, null, null);

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(adminUsernamePasswordAuthenticationToken);

        // When
        Account actual = authenticationService.getSignedInAccount();

        // Then
        Assertions.assertEquals(adminAccount, actual);
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testGetSignedInAccountShouldThrowExceptionWhenNoBodyIsSignedIn() {
        // Given
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(null);

        // When
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> authenticationService.getSignedInAccount());

        // Then
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testGetSignedInAccountShouldThrowExceptionWhenThePrincipalIsNotSupported() {
        // Given
        UsernamePasswordAuthenticationToken wrongUsernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(new Object(), null, null);

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(wrongUsernamePasswordAuthenticationToken);

        // When
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> authenticationService.getSignedInAccount());

        // Then
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testIsSignedInAccountAdminShouldReturnTrueWhenAdminIsSignedIn() {
        // Given
        Account adminAccount = new Account(200L, "admin",
                "$2y$04$LAI2hWUb1WB7hlnSfHCAEuqkybgnr7RKLJrBIi5m4gp6OOUEwCvmi", true);
        UserDetails adminAccountDetails = new AccountDetails(adminAccount);
        UsernamePasswordAuthenticationToken adminUsernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(adminAccountDetails, null, null);

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(adminUsernamePasswordAuthenticationToken);

        // When
        Assertions.assertTrue(authenticationService.isSignedInAccountAdmin());

        // Then
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testIsSignedInAccountAdminShouldReturnFalseWhenUserIsSignedIn() {
        // Given
        Account userAccount = new Account(100L, "test",
                "$2y$04$4xBBVeTKzpGQfnSnaY7CB.rYzcOAcX62f3mjNqKmlE/29sCx0x5wC", false);
        UserDetails userAccountDetails = new AccountDetails(userAccount);
        UsernamePasswordAuthenticationToken userUsernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userAccountDetails, null, null);

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(userUsernamePasswordAuthenticationToken);

        // When
        Assertions.assertFalse(authenticationService.isSignedInAccountAdmin());

        // Then
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testIsSignedInAccountAdminShouldReturnFalseWhenNoBodyIsSignedIn() {
        // Given
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(null);

        // When
        Assertions.assertFalse(authenticationService.isSignedInAccountAdmin());

        // Then
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }
}
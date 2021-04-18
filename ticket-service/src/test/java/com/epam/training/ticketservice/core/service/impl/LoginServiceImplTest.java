package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.entity.Account;
import com.epam.training.ticketservice.core.service.AuthenticationService;
import com.epam.training.ticketservice.core.service.LoginService;
import com.epam.training.ticketservice.core.service.model.AccountDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

    private LoginService loginService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private AuthenticationService authenticationService;

    private Account adminAccount;
    private AccountDetails adminAccountDetails;

    private Account userAccount;

    @BeforeEach
    public void init() {
        loginService = new LoginServiceImpl(passwordEncoder, userDetailsService, authenticationService);

        adminAccount = new Account();
        adminAccount.setId(200L);
        adminAccount.setUsername("admin");
        adminAccount.setPassword("$2y$04$LAI2hWUb1WB7hlnSfHCAEuqkybgnr7RKLJrBIi5m4gp6OOUEwCvmi");

        adminAccountDetails = new AccountDetails(adminAccount);

        userAccount = new Account(100L, "test",
                "$2y$04$4xBBVeTKzpGQfnSnaY7CB.rYzcOAcX62f3mjNqKmlE/29sCx0x5wC");
    }

    @Test
    public void testSignInPrivilegedShouldSignInWhenTheRightAdminCredentialsUsed() {
        // Given
        String username = "admin";
        String password = "admin";

        Mockito.when(userDetailsService
                .loadUserByUsername(adminAccount.getUsername()))
                .thenReturn(adminAccountDetails);
        Mockito.when(passwordEncoder
                .matches(password, adminAccount.getPassword()))
                .thenReturn(true);

        // When
        loginService.signInPrivileged(username, password);

        // Then
        Mockito.verify(userDetailsService).loadUserByUsername(adminAccount.getUsername());
        Mockito.verify(passwordEncoder).matches(password, adminAccount.getPassword());
        Mockito.verify(authenticationService).setAuthentication(adminAccountDetails);
        Mockito.verifyNoMoreInteractions(userDetailsService, passwordEncoder, authenticationService);
    }

    @Test
    public void testSignInPrivilegedShouldThrowExceptionWhenAdminUsesWrongPassword() {
        // Given
        String username = "admin";
        String wrongPassword = "notAdmin";

        Mockito.when(userDetailsService
                .loadUserByUsername(adminAccount.getUsername()))
                .thenReturn(adminAccountDetails);
        Mockito.when(passwordEncoder
                .matches(wrongPassword, adminAccount.getPassword()))
                .thenReturn(false);

        // When
        Assertions.assertThrows(BadCredentialsException.class,
                () -> loginService.signInPrivileged(username, wrongPassword));

        // Then
        Mockito.verify(userDetailsService).loadUserByUsername(adminAccount.getUsername());
        Mockito.verify(passwordEncoder).matches(wrongPassword, adminAccount.getPassword());
        Mockito.verifyNoMoreInteractions(userDetailsService, passwordEncoder);
    }

    @Test
    public void testSignOutShouldSignOutTheAccount() {
        // When
        loginService.signOut();

        // Then
        Mockito.verify(authenticationService).clearAuthentication();
        Mockito.verifyNoMoreInteractions(authenticationService);
    }

    @Test
    public void testDescribeAccountShouldReturnsExpectedResultWhenAdminIsSignedIn() {
        // Given
        String expected = "Signed in with privileged account '" + adminAccount.getUsername() + "'";

        Mockito.when(authenticationService.getSignedInAccount()).thenReturn(adminAccount);

        // When
        String actual = loginService.describeAccount();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(authenticationService).getSignedInAccount();
        Mockito.verifyNoMoreInteractions(authenticationService);
    }

    @Test
    public void testDescribeAccountShouldReturnsExpectedResultWhenUserIsSignedIn() {
        // Given
        String expected = "Signed in with account '" + userAccount.getUsername() + "'";

        Mockito.when(authenticationService.getSignedInAccount()).thenReturn(userAccount);

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

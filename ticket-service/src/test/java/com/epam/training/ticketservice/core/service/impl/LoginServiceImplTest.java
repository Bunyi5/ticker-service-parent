package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.entity.Account;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

    private LoginService loginService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserDetailsService userDetailsService;

    private Account adminAccount;
    private AccountDetails adminAccountDetails;
    private UsernamePasswordAuthenticationToken adminUsernamePasswordAuthenticationToken;

    @BeforeEach
    public void init() {
        loginService = new LoginServiceImpl(passwordEncoder, userDetailsService);

        adminAccount = new Account();
        adminAccount.setId(200L);
        adminAccount.setUsername("admin");
        adminAccount.setPassword("$2y$04$LAI2hWUb1WB7hlnSfHCAEuqkybgnr7RKLJrBIi5m4gp6OOUEwCvmi");

        adminAccountDetails = new AccountDetails(adminAccount);

        adminUsernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(adminAccountDetails, null, null);
    }

    @Test
    public void testSignInPrivilegedShouldSignInWhenTheRightAdminCredentialsUsed() {
        // Given
        String username = "admin";
        String password = "admin";

        SecurityContextHolder.setContext(securityContext);
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
        Mockito.verify(securityContext).setAuthentication(adminUsernamePasswordAuthenticationToken);
        Mockito.verifyNoMoreInteractions(userDetailsService, passwordEncoder, securityContext);
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
        // Given
        SecurityContextHolder.setContext(securityContext);

        // When
        loginService.signOut();

        // Then
        Mockito.verify(securityContext).setAuthentication(null);
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testDescribeAccountShouldReturnsExpectedResultWhenAdminIsSignedIn() {
        // Given
        String expected = "Signed in with privileged account '" + adminAccount.getUsername() + "'";

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(adminUsernamePasswordAuthenticationToken);

        // When
        String actual = loginService.describeAccount();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testDescribeAccountShouldReturnsExpectedResultWhenUserIsSignedIn() {
        // Given
        Account userAccount = new Account(100L, "test", "test");
        String expected = "Signed in with account '" + userAccount.getUsername() + "'";
        AccountDetails userAccountDetails = new AccountDetails(userAccount);

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        userAccountDetails,
                        null,
                        null
                ));

        // When
        String actual = loginService.describeAccount();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testDescribeAccountShouldReturnsExpectedResultWhenNoBodyIsSignedIn() {
        // Given
        String expected = "You are not signed in";

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);

        // When
        String actual = loginService.describeAccount();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testGetSignedInAccountShouldReturnsExpectedResultWhenAdminIsSignedIn() {
        // Given
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(adminUsernamePasswordAuthenticationToken);

        // When
        Account actual = loginService.getSignedInAccount();

        // Then
        Assertions.assertEquals(adminAccount, actual);
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testGetSignedInAccountShouldThrowExceptionWhenNoBodyIsSignedIn() {
        // Given
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);

        // When
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> loginService.getSignedInAccount());

        // Then
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }

    @Test
    public void testGetSignedInAccountShouldThrowExceptionWhenThePrincipalIsNotSupported() {
        // Given
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        new Object(),
                        null,
                        null
                ));

        // When
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> loginService.getSignedInAccount());

        // Then
        Mockito.verify(securityContext).getAuthentication();
        Mockito.verifyNoMoreInteractions(securityContext);
    }
}

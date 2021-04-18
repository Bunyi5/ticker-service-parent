package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.AccountRepository;
import com.epam.training.ticketservice.core.persistence.entity.Account;
import com.epam.training.ticketservice.core.service.model.AccountDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    private UserDetailsService userDetailsService;

    @Mock
    private AccountRepository accountRepository;

    private Account adminAccount;

    @BeforeEach
    public void init() {
        userDetailsService = new UserDetailsServiceImpl(accountRepository);

        adminAccount = new Account();
        adminAccount.setId(200L);
        adminAccount.setUsername("admin");
        adminAccount.setPassword("$2y$04$LAI2hWUb1WB7hlnSfHCAEuqkybgnr7RKLJrBIi5m4gp6OOUEwCvmi");
    }

    @Test
    public void testLoadUserByUsernameShouldReturnsExpectedResultWhenLoadsAdmin() {
        // Given
        String username = "admin";
        UserDetails expected = new AccountDetails(adminAccount);

        Mockito.when(accountRepository
                .findByUsername(username))
                .thenReturn(Optional.of(adminAccount));

        // When
        UserDetails actual = userDetailsService.loadUserByUsername(username);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(accountRepository).findByUsername(username);
        Mockito.verifyNoMoreInteractions(accountRepository);
    }

    @Test
    public void testLoadUserByUsernameShouldThrowExceptionWhenUserIsNotExists() {
        // Given
        String wrongUsername = "test";
        Mockito.when(accountRepository
                .findByUsername(wrongUsername))
                .thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(wrongUsername));

        // Then
        Mockito.verify(accountRepository).findByUsername(wrongUsername);
        Mockito.verifyNoMoreInteractions(accountRepository);
    }
}
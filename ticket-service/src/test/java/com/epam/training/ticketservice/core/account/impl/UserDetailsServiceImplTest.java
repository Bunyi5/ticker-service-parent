package com.epam.training.ticketservice.core.account.impl;

import com.epam.training.ticketservice.core.account.model.AccountDetails;
import com.epam.training.ticketservice.core.account.persistence.entity.Account;
import com.epam.training.ticketservice.core.account.persistence.repository.AccountRepository;
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

    @BeforeEach
    public void init() {
        userDetailsService = new UserDetailsServiceImpl(accountRepository);
    }

    @Test
    public void testLoadUserByUsernameShouldReturnExpectedResultWhenLoadsAdmin() {
        // Given
        Account adminAccount = new Account(200L, "admin",
                "$2y$04$LAI2hWUb1WB7hlnSfHCAEuqkybgnr7RKLJrBIi5m4gp6OOUEwCvmi", true);
        UserDetails adminAccountDetails = new AccountDetails(adminAccount);

        Mockito.when(accountRepository.findByUsername(adminAccount.getUsername()))
                .thenReturn(Optional.of(adminAccount));

        // When
        UserDetails actual = userDetailsService.loadUserByUsername(adminAccount.getUsername());

        // Then
        Assertions.assertEquals(adminAccountDetails, actual);
        Mockito.verify(accountRepository).findByUsername(adminAccount.getUsername());
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
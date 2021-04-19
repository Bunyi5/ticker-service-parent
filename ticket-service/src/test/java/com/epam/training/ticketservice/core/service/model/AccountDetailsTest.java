package com.epam.training.ticketservice.core.service.model;

import com.epam.training.ticketservice.core.persistence.entity.Account;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountDetailsTest {

    private AccountDetails accountDetails;

    private Account adminAccount;

    @BeforeEach
    public void init() {
        adminAccount = new Account(200L, "admin",
                "$2y$04$LAI2hWUb1WB7hlnSfHCAEuqkybgnr7RKLJrBIi5m4gp6OOUEwCvmi", true);
        accountDetails = new AccountDetails(adminAccount);
    }

    @Test
    public void testGetAuthoritiesShouldReturnNull() {
        // When
        Assertions.assertNull(accountDetails.getAuthorities());
    }

    @Test
    public void testGetPasswordShouldReturnExpected() {
        // When
        String actual = accountDetails.getPassword();

        // Then
        Assertions.assertEquals(adminAccount.getPassword(), actual);
    }

    @Test
    public void testGetUsernameShouldReturnExpected() {
        // When
        String actual = accountDetails.getUsername();

        // Then
        Assertions.assertEquals(adminAccount.getUsername(), actual);
    }

    @Test
    public void testIsAccountNonExpiredShouldReturnTrue() {
        // When
        Assertions.assertTrue(accountDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLockedShouldReturnTrue() {
        // When
        Assertions.assertTrue(accountDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpiredShouldReturnTrue() {
        // When
        Assertions.assertTrue(accountDetails.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabledShouldReturnTrue() {
        // When
        Assertions.assertTrue(accountDetails.isEnabled());
    }

    @Test
    public void getAccount() {
        // When
        Account actual = accountDetails.getAccount();

        // Then
        Assertions.assertEquals(adminAccount, actual);
    }

    @Test
    public void testEqualsAndHashCode() {
        // When
        EqualsVerifier.simple().forClass(AccountDetails.class).verify();
    }

    @Test
    public void testToStringShouldReturnExpectedResult() {
        // Given
        String expected = "AccountDetails(account=" + adminAccount.toString() + ")";

        // When
        String actual = accountDetails.toString();
        Assertions.assertEquals(expected, actual);
    }
}
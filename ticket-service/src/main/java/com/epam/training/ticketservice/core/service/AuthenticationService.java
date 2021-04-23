package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.core.persistence.entity.Account;

public interface AuthenticationService {

    void signIn(String username, String password);

    void clearAuthentication();

    Account getSignedInAccount();

    boolean isSignedInAccountAdmin();
}

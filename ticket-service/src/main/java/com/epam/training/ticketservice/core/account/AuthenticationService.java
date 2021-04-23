package com.epam.training.ticketservice.core.account;

import com.epam.training.ticketservice.core.account.persistence.entity.Account;

public interface AuthenticationService {

    void signIn(String username, String password);

    void clearAuthentication();

    Account getSignedInAccount();

    boolean isSignedInAccountAdmin();
}

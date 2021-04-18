package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.core.persistence.entity.Account;

public interface LoginService {

    void signInPrivileged(String username, String password);

    void signOut();

    String describeAccount();

    Account getSignedInAccount();
}

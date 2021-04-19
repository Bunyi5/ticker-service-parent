package com.epam.training.ticketservice.core.service;

public interface LoginService {

    void signInPrivileged(String username, String password);

    void signOut();

    String describeAccount();
}

package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.core.persistence.entity.Account;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    void setAuthentication(UserDetails userDetails);

    void clearAuthentication();

    Account getSignedInAccount();

    boolean isSignedInAccountAdmin();
}

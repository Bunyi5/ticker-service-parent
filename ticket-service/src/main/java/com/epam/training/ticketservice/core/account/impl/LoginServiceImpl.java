package com.epam.training.ticketservice.core.account.impl;

import com.epam.training.ticketservice.core.account.persistence.entity.Account;
import com.epam.training.ticketservice.core.account.AuthenticationService;
import com.epam.training.ticketservice.core.account.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationService authenticationService;

    @Override
    public void signInPrivileged(String username, String password) throws BadCredentialsException {
        authenticationService.signIn(username, password);
    }

    @Override
    public void signOut() {
        authenticationService.clearAuthentication();
    }

    @Override
    public String describeAccount() {
        try {
            Account account = authenticationService.getSignedInAccount();

            if (account.isAdmin()) {
                return "Signed in with privileged account '" + account.getUsername() + "'";
            } else {
                return "Signed in with account '" + account.getUsername() + "'";
            }
        } catch (AuthenticationCredentialsNotFoundException e) {
            return "You are not signed in";
        }
    }
}

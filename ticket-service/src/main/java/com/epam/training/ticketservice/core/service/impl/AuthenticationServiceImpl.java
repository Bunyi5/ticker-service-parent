package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.entity.Account;
import com.epam.training.ticketservice.core.service.AuthenticationService;
import com.epam.training.ticketservice.core.service.model.AccountDetails;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public void setAuthentication(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, null);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Override
    public void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public Account getSignedInAccount() throws AuthenticationCredentialsNotFoundException {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("You are not signed in");
        }

        AccountDetails authPrinciple = getAuthPrinciple(authentication.getPrincipal());
        return authPrinciple.getAccount();
    }

    @Override
    public boolean isSignedInAccountAdmin() {
        try {
            Account account = getSignedInAccount();
            return account.isAccountAdmin();
        } catch (AuthenticationCredentialsNotFoundException e) {
            return false;
        }
    }

    private AccountDetails getAuthPrinciple(Object principal) {
        if (principal instanceof AccountDetails) {
            return (AccountDetails) principal;
        } else {
            throw new IllegalArgumentException("Principal type is not supported!");
        }
    }
}

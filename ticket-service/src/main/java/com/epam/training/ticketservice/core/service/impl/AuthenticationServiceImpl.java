package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.entity.Account;
import com.epam.training.ticketservice.core.service.AuthenticationService;
import com.epam.training.ticketservice.core.service.model.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public void signIn(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            setAuthentication(userDetails);
        } else {
            throw new BadCredentialsException("Incorrect password!");
        }
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
            return account.isAdmin();
        } catch (AuthenticationCredentialsNotFoundException e) {
            return false;
        }
    }

    private void setAuthentication(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, null);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private AccountDetails getAuthPrinciple(Object principal) {
        if (principal instanceof AccountDetails) {
            return (AccountDetails) principal;
        } else {
            throw new IllegalArgumentException("Principal type is not supported!");
        }
    }
}

package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.persistence.entity.Account;
import com.epam.training.ticketservice.core.service.LoginService;
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
public class LoginServiceImpl implements LoginService {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public void signInPrivileged(String username, String password) throws BadCredentialsException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, null);

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } else {
            throw new BadCredentialsException("Incorrect password!");
        }
    }

    @Override
    public void signOut() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public String describeAccount() {
        try {
            Account account = getSignedInAccount();

            if (isAccountAdmin(account)) {
                return "Signed in with privileged account '" + account.getUsername() + "'";
            } else {
                return "Signed in with account '" + account.getUsername() + "'";
            }
        } catch (AuthenticationCredentialsNotFoundException e) {
            return "You are not signed in";
        }
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

    private boolean isAccountAdmin(Account account) {
        return account.getUsername().equals("admin");
    }

    private AccountDetails getAuthPrinciple(Object principal) {
        if (principal instanceof AccountDetails) {
            return (AccountDetails) principal;
        } else {
            throw new IllegalArgumentException("Principal type is not supported!");
        }
    }
}

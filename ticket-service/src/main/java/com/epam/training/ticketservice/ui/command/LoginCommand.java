package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.service.AvailabilityService;
import com.epam.training.ticketservice.core.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class LoginCommand {

    private final LoginService loginService;
    private final AvailabilityService availabilityService;

    @ShellMethod(value = "Sign in privileged", key = "sign in privileged")
    public String signInPrivileged(@ShellOption String username, @ShellOption String password) {
        try {
            loginService.signInPrivileged(username, password);
        } catch (BadCredentialsException e) {
            return "Login failed due to incorrect credentials";
        }
        return null;
    }

    @ShellMethod(value = "Sign out", key = "sign out")
    public void signOut() {
        loginService.signOut();
    }

    @ShellMethod(value = "Describe account", key = "describe account")
    public String describeAccount() {
        return loginService.describeAccount();
    }

    @ShellMethodAvailability("sign out")
    public Availability isThereASignedInAccount() {
        return availabilityService.isThereASignedInAccount();
    }
}

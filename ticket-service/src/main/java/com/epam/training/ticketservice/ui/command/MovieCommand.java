package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.service.AuthenticationService;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.core.service.model.MovieDto;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class MovieCommand {

    private final MovieService movieService;
    private final AuthenticationService authenticationService;

    @ShellMethodAvailability("isSignedInAccountAdmin")
    @ShellMethod(value = "Create a movie", key = "create movie")
    public void createMovie(@ShellOption String title, @ShellOption String genre, @ShellOption int minutes) {
        movieService.createMovie(title, genre, minutes);
    }

    @ShellMethodAvailability("isSignedInAccountAdmin")
    @ShellMethod(value = "Update a movie", key = "update movie")
    public void updateMovie(@ShellOption String title, @ShellOption String genre, @ShellOption int minutes) {
        movieService.updateMovie(title, genre, minutes);
    }

    @ShellMethodAvailability("isSignedInAccountAdmin")
    @ShellMethod(value = "Delete a movie", key = "delete movie")
    public void deleteMovie(@ShellOption String title) {
        movieService.deleteMovie(title);
    }

    @ShellMethod(value = "List movies", key = "list movies")
    public MovieDto listMovies() {
        return movieService.getMovieDto();
    }

    public Availability isSignedInAccountAdmin() {
        return authenticationService.isSignedInAccountAdmin()
                ? Availability.available()
                : Availability.unavailable("You are not an admin");
    }
}

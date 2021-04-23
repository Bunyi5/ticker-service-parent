package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.service.AvailabilityService;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.core.service.model.MovieDto;
import com.epam.training.ticketservice.ui.command.model.MovieDtoList;
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
    private final AvailabilityService availabilityService;

    @ShellMethod(value = "Create a movie", key = "create movie")
    public MovieDto createMovie(@ShellOption String title, @ShellOption String genre, @ShellOption int length) {
        MovieDto movieDto = buildMovieDto(title, genre, length);
        movieService.createMovie(movieDto);
        return movieDto;
    }

    @ShellMethod(value = "Update a movie", key = "update movie")
    public MovieDto updateMovie(@ShellOption String title, @ShellOption String genre, @ShellOption int length) {
        MovieDto movieDto = buildMovieDto(title, genre, length);
        movieService.updateMovie(movieDto);
        return movieDto;
    }

    @ShellMethod(value = "Delete a movie", key = "delete movie")
    public void deleteMovie(@ShellOption String title) {
        movieService.deleteMovie(title);
    }

    @ShellMethod(value = "List movies", key = "list movies")
    public MovieDtoList listMovies() {
        return movieService.getMovieDtoList();
    }

    @ShellMethodAvailability({"create movie", "update movie", "delete movie"})
    public Availability isSignedInAccountAdmin() {
        return availabilityService.isSignedInAccountAdmin();
    }

    private MovieDto buildMovieDto(String title, String genre, int length) {
        return MovieDto.builder()
                .title(title)
                .genre(genre)
                .length(length)
                .build();
    }
}

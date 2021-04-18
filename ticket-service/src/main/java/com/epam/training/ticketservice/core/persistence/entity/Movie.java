package com.epam.training.ticketservice.core.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String title;
    private String genre;
    private int minutes;

    public Movie(String title, String genre, int minutes) {
        this.title = title;
        this.genre = genre;
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return title + " (" + genre + ", " + minutes + " minutes)";
    }
}

package com.epam.training.ticketservice.core.persistence;

import com.epam.training.ticketservice.core.persistence.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);

    @Transactional
    void deleteByTitle(String title);
}

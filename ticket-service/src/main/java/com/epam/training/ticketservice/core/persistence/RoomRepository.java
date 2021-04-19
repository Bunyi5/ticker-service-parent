package com.epam.training.ticketservice.core.persistence;

import com.epam.training.ticketservice.core.persistence.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomName(String name);

    @Transactional
    void deleteByRoomName(String name);
}

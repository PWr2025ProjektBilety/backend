package com.example.backend.ticket.repository;

import com.example.backend.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Override
    Optional<Ticket> findById(Long id);

    @Query(value = "SELECT * FROM ticket", nativeQuery = true)
    List<Ticket> findAllIncludingInactive();
}

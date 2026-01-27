package com.example.backend.repository.ticket;

import com.example.backend.model.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByIsActiveTrue();

    @Override
    Optional<Ticket> findById(Long id);

    @Query("SELECT t FROM Ticket t")
    List<Ticket> findAllIncludingInactive();
}

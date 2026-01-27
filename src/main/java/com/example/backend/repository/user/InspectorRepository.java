package com.example.backend.repository.user;

import com.example.backend.model.user.TicketInspector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InspectorRepository extends JpaRepository<TicketInspector, Long> {

    Optional<TicketInspector> findByLogin(String login);
}

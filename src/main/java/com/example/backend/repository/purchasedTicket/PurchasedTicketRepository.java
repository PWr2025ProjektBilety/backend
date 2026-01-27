package com.example.backend.repository.purchasedTicket;

import com.example.backend.model.purchasedTicket.PurchasedTicket;
import com.example.backend.model.user.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchasedTicketRepository extends JpaRepository<PurchasedTicket, Long> {
    Optional<PurchasedTicket> findByCode(String code);
    Page<PurchasedTicket> findAllByPassenger(Passenger passenger, Pageable pageable);
}

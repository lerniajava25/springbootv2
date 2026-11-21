package org.example.helpdesk.repository;

import org.example.helpdesk.entity.Ticket;
import org.example.helpdesk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByOwnerOrderByCreatedAtDesc(User owner);
    List<Ticket> findAllByOrderByCreatedAtDesc();
}

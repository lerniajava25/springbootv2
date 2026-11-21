package org.example.helpdesk.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4000)
    private String text;

    @ManyToOne(optional = false)
    private Ticket ticket;

    @ManyToOne(optional = false)
    private User author;

    private LocalDateTime createdAt = LocalDateTime.now();

    protected Comment() {
    }

    public Comment(String text, Ticket ticket, User author) {
        this.text = text;
        this.ticket = ticket;
        this.author = author;
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public Ticket getTicket() { return ticket; }
    public User getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

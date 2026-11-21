package org.example.helpdesk.controller;

import org.example.helpdesk.entity.Comment;
import org.example.helpdesk.entity.Ticket;
import org.example.helpdesk.entity.TicketStatus;
import org.example.helpdesk.entity.User;
import org.example.helpdesk.repository.CommentRepository;
import org.example.helpdesk.repository.TicketRepository;
import org.example.helpdesk.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class TicketController {
    private final TicketRepository tickets;
    private final CommentRepository comments;
    private final UserRepository users;

    public TicketController(TicketRepository tickets, CommentRepository comments, UserRepository users) {
        this.tickets = tickets;
        this.comments = comments;
        this.users = users;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/tickets";
    }

    @GetMapping("/tickets")
    public String listTickets(Authentication authentication, Model model) {
        User currentUser = currentUser(authentication);
        boolean staff = isStaff(authentication);
        model.addAttribute("tickets", staff
            ? tickets.findAllByOrderByCreatedAtDesc()
            : tickets.findByOwnerOrderByCreatedAtDesc(currentUser));
        model.addAttribute("staff", staff);
        return "tickets/list";
    }

    @GetMapping("/tickets/new")
    public String newTicket(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "tickets/form";
    }

    @PostMapping("/tickets")
    public String createTicket(@RequestParam String title,
                               @RequestParam String description,
                               Authentication authentication) {
        Ticket ticket = new Ticket(title, description, currentUser(authentication));
        tickets.save(ticket);
        return "redirect:/tickets/" + ticket.getId();
    }

    @GetMapping("/tickets/{id}")
    public String ticketDetails(@PathVariable Long id, Authentication authentication, Model model) {
        Ticket ticket = getAllowedTicket(id, authentication);
        model.addAttribute("ticket", ticket);
        model.addAttribute("statuses", TicketStatus.values());
        model.addAttribute("staff", isStaff(authentication));
        return "tickets/detail";
    }

    @PostMapping("/tickets/{id}/comments")
    public String addComment(@PathVariable Long id,
                             @RequestParam String text,
                             @RequestParam(defaultValue = "false") boolean reopen,
                             Authentication authentication) {
        Ticket ticket = getAllowedTicket(id, authentication);
        boolean staff = isStaff(authentication);

        if (!staff && (ticket.getStatus() == TicketStatus.RESOLVED
            || ticket.getStatus() == TicketStatus.CLOSED)
            && !reopen) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "The ticket must be reopened before a comment can be added.");
        }

        if (!staff && reopen
            && (ticket.getStatus() == TicketStatus.RESOLVED
            || ticket.getStatus() == TicketStatus.CLOSED)) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }

        comments.save(new Comment(text, ticket, currentUser(authentication)));
        tickets.save(ticket);
        return "redirect:/tickets/" + id;
    }

    @PostMapping("/agent/tickets/{id}/assign")
    public String assignTicket(@PathVariable Long id, Authentication authentication) {
        Ticket ticket = tickets.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ticket.setAssignee(currentUser(authentication));
        if (ticket.getStatus() == TicketStatus.NEW) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }
        tickets.save(ticket);
        return "redirect:/tickets/" + id;
    }

    @PostMapping("/agent/tickets/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam TicketStatus status) {
        Ticket ticket = tickets.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ticket.setStatus(status);
        tickets.save(ticket);
        return "redirect:/tickets/" + id;
    }

    private Ticket getAllowedTicket(Long id, Authentication authentication) {
        Ticket ticket = tickets.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (isStaff(authentication) || ticket.getOwner().getId().equals(currentUser(authentication).getId())) {
            return ticket;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    private User currentUser(Authentication authentication) {
        return users.findByUsername(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    private boolean isStaff(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_AGENT")
                || a.getAuthority().equals("ROLE_ADMIN"));
    }
}

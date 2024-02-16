package br.com.fullcycle.hexagonal.application.domain;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Event {

    private static final int ONE = 1;

    private final EventId eventId;
    private Name name;
    private LocalDate date;
    private int totalSpots;
    private PartnerId partnerId;
    private Set<EventTicket> tickets;

    public Event(final EventId eventId, final String name, final String date, final Integer totalSpots, final PartnerId partnerId) {
        this(eventId);
        this.setName(name);
        this.setDate(date);
        this.setTotalSpots(totalSpots);
        this.setPartnerId(partnerId);
    }

    private Event(final EventId eventId) {
        if (eventId == null) {
            throw new ValidationException("Invalid eventId for Event");
        }
        this.eventId = eventId;
        this.tickets = new HashSet<>(0);
    }

    public static Event newEvent(final String name, final String date, final Integer totalSpots, final Partner partner) {
        return new Event(EventId.unique(), name, date, totalSpots, partner.getPartnerId());
    }

    public Ticket reserveTicket(final CustomerId customerId) {
        allTickets().stream()
                .filter(it -> Objects.equals(it.customerId(), customerId))
                .findFirst()
                .ifPresent(it -> {
                    throw new ValidationException("Ticket already registered");
                });
        if (getTotalSpots() < allTickets().size() + ONE) {
            throw new ValidationException("Event sold out");
        }

        final var newTicket = Ticket.newTicket(customerId, getEventId());

        this.tickets.add(new EventTicket(newTicket.getTicketId(), getEventId(), customerId, allTickets().size() + ONE));

        return newTicket;
    }

    public EventId getEventId() {
        return eventId;
    }

    public Name getName() {
        return name;
    }

    private void setName(final String name) {
        this.name = new Name(name);
    }

    public LocalDate getDate() {
        return date;
    }

    private void setDate(final String date) {
        if (date == null) {
            throw new ValidationException("Invalid date for Event");
        }
        this.date = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    private void setTotalSpots(final Integer totalSpots) {
        if (totalSpots == null) {
            throw new ValidationException("Invalid totalSpots for Event");
        }
        this.totalSpots = totalSpots;
    }

    public PartnerId getPartnerId() {
        return partnerId;
    }

    private void setPartnerId(PartnerId partnerId) {
        if (partnerId == null) {
            throw new ValidationException("Invalid partnerId for Event");
        }
        this.partnerId = partnerId;
    }

    public Set<EventTicket> allTickets() {
        return Collections.unmodifiableSet(tickets);
    }
}
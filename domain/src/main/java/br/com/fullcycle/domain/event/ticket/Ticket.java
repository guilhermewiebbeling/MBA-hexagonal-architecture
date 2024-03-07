package br.com.fullcycle.domain.event.ticket;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventTicketId;
import br.com.fullcycle.domain.event.TicketCreated;
import br.com.fullcycle.domain.exceptions.ValidationException;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Ticket {

    private final TicketId ticketId;
    private CustomerId customerId;
    private EventId eventId;
    private TicketStatus status;
    private Instant paidAt;
    private Instant reservedAt;
    private final Set<DomainEvent> domainEvents;

    public Ticket(
            final TicketId ticketId,
            final CustomerId customerId,
            final EventId eventId,
            final TicketStatus status,
            final Instant paidAt,
            final Instant reservedAt
    ) {
        this.ticketId = ticketId;
        this.setCustomerId(customerId);
        this.setEventId(eventId);
        this.setStatus(status);
        this.setPaidAt(paidAt);
        this.setReservedAt(reservedAt);
        this.domainEvents = new HashSet<>();
    }

    public static Ticket newTicket(final CustomerId customerId, final EventId eventId) {
        return new Ticket(TicketId.unique(), customerId, eventId, TicketStatus.PENDING, null, Instant.now());
    }

    public static Ticket newTicket(final EventTicketId eventTicketId, final CustomerId customerId, final EventId eventId) {
        Ticket aTicket = newTicket(customerId, eventId);
        aTicket.domainEvents.add(new TicketCreated(aTicket.ticketId, eventTicketId, eventId, customerId));
        return aTicket;
    }

    public TicketId getTicketId() {
        return ticketId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public EventId getEventId() {
        return eventId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    private void setCustomerId(final CustomerId customerId) {
        if (customerId == null) {
            throw new ValidationException("Invalid customerId for Ticket");
        }
        this.customerId = customerId;
    }

    private void setEventId(final EventId eventId) {
        if (eventId == null) {
            throw new ValidationException("Invalid eventId for Ticket");
        }
        this.eventId = eventId;
    }

    private void setStatus(final TicketStatus status) {
        if (status == null) {
            throw new ValidationException("Invalid status for Ticket");
        }
        this.status = status;
    }

    private void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    private void setReservedAt(Instant reservedAt) {
        if (reservedAt == null) {
            throw new ValidationException("Invalid reservedAt for Ticket");
        }
        this.reservedAt = reservedAt;
    }

    public Set<DomainEvent> allDomainEvents() {
        return Collections.unmodifiableSet(domainEvents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketId, ticket.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }
}
package br.com.fullcycle.domain.event.ticket;

import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> ticketOfId(TicketId id);

    Ticket create(Ticket event);

    Ticket update (Ticket event);

    void deleteAll();
}

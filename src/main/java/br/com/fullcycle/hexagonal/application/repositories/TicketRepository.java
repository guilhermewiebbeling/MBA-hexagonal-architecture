package br.com.fullcycle.hexagonal.application.repositories;

import br.com.fullcycle.hexagonal.application.domain.Ticket;
import br.com.fullcycle.hexagonal.application.domain.TicketId;

import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> ticketOfId(TicketId id);

    Ticket create(Ticket event);

    Ticket update (Ticket event);
}

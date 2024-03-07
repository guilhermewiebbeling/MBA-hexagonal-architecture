package br.com.fullcycle.domain.event;

import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

public class EventTest {

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreateEvent() {
        //given
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = aPartner.getPartnerId().value();
        final var expectedTickets = 0;

        //when
        final var actualEvent = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, aPartner);

        //then
        Assertions.assertNotNull(actualEvent.getEventId());
        Assertions.assertEquals(expectedName, actualEvent.getName().value());
        Assertions.assertEquals(expectedDate, actualEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        Assertions.assertEquals(expectedTotalSpots, actualEvent.getTotalSpots());
        Assertions.assertEquals(expectedPartnerId, actualEvent.getPartnerId().value());
        Assertions.assertEquals(expectedTickets, actualEvent.allTickets().size());
    }

    @Test
    @DisplayName("Não deve criar um evento com nome inválido")
    public void testCreateEventWithInvalidName() {
        //given
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var expectedException = "Invalid value for Name";

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> Event.newEvent(null, "2021-01-01", 10, aPartner));

        //then
        Assertions.assertEquals(expectedException, actualException.getMessage());
    }

    @Test
    @DisplayName("Deve reservar um ticket quando é possível")
    public void testReserveTicket() {
        //given
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var aCustomer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");
        final var expectedCustomerId = aCustomer.getCustomerId();
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = aPartner.getPartnerId().value();
        final var expectedTickets = 1;
        final var expectedTicketOrder = 1;
        final var expectedDomainEvent = "event-ticket-reserved";

        final var actualEvent = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, aPartner);
        final var expectedEventId = actualEvent.getEventId();

        //when
        final var actualTicket = actualEvent.reserveTicket(aCustomer.getCustomerId());

        //then
        Assertions.assertNotNull(actualTicket.getEventTicketId());
        Assertions.assertNull(actualTicket.getTicketId());
        Assertions.assertEquals(expectedEventId, actualTicket.getEventId());
        Assertions.assertEquals(expectedCustomerId, actualTicket.getCustomerId());

        Assertions.assertEquals(expectedName, actualEvent.getName().value());
        Assertions.assertEquals(expectedDate, actualEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        Assertions.assertEquals(expectedTotalSpots, actualEvent.getTotalSpots());
        Assertions.assertEquals(expectedPartnerId, actualEvent.getPartnerId().value());
        Assertions.assertEquals(expectedTickets, actualEvent.allTickets().size());

        final var actualEventTicket = actualEvent.allTickets().iterator().next();
        Assertions.assertEquals(expectedTicketOrder, actualEventTicket.getOrdering());
        Assertions.assertEquals(expectedEventId, actualEventTicket.getEventId());
        Assertions.assertEquals(expectedCustomerId, actualEventTicket.getCustomerId());
        Assertions.assertEquals(actualTicket.getTicketId(), actualEventTicket.getTicketId());

        final var actualDomainEvent = actualEvent.allDomainEvents().iterator().next();
        Assertions.assertEquals(expectedDomainEvent, actualDomainEvent.type());
    }

    @Test
    @DisplayName("Não deve reservar um ticket quando o evento está esgotado")
    public void testReserveTicketWhenEventIsSoldOut() throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");

        final var aCustomer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");

        final var aCustomer2 = Customer.newCustomer("John1 Doe", "111.456.789-01", "john1.doe@gmail.com");


        final var expectedTotalSpots = 1;
        final var expectedError = "Event sold out";

        final var actualEvent = Event.newEvent("Disney on Ice", "2021-01-01", expectedTotalSpots, aPartner);

        actualEvent.reserveTicket(aCustomer.getCustomerId());

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> actualEvent.reserveTicket(aCustomer2.getCustomerId()));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve reservar dois tickets para um mesmo cliente")
    public void testReserveTwoTicketsForTheSameClient() throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");

        final var aCustomer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");

        final var expectedTotalSpots = 1;
        final var expectedError = "Ticket already registered";

        final var actualEvent = Event.newEvent("Disney on Ice", "2021-01-01", expectedTotalSpots, aPartner);

        actualEvent.reserveTicket(aCustomer.getCustomerId());

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> actualEvent.reserveTicket(aCustomer.getCustomerId()));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }
}

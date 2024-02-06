package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.models.Event;
import br.com.fullcycle.hexagonal.models.Ticket;
import br.com.fullcycle.hexagonal.models.TicketStatus;
import br.com.fullcycle.hexagonal.services.CustomerService;
import br.com.fullcycle.hexagonal.services.EventService;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SubscribeCustomerToEventUseCaseTest {

    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() throws Exception {
        //given
        final var expectedTicketsSize = 1;
        final var customerID = TSID.fast().toLong();
        final var eventID = TSID.fast().toLong();

        final var aCustomer = new Customer();
        aCustomer.setId(customerID);
        aCustomer.setCpf("123456789");
        aCustomer.setEmail("john.dutton@gmail.com");
        aCustomer.setName("John Dutton");

        final var aEvent = new Event();
        aEvent.setId(eventID);
        aEvent.setName("Disney");
        aEvent.setTotalSpots(10);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(eventID, customerID);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(customerService.findById(customerID)).thenReturn(Optional.of(aCustomer));
        when(eventService.findById(eventID)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventID, customerID)).thenReturn(Optional.empty());
        when(eventService.save(any())).thenAnswer(a -> {
            final var e = a.getArgument(0, Event.class);
            Assertions.assertEquals(expectedTicketsSize, e.getTickets().size());
            return e;
        });

        final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
        final var output = useCase.execute(subscribeInput);

        //then
        Assertions.assertEquals(eventID, output.eventId());
        Assertions.assertNotNull(output.reservationDate());
        Assertions.assertEquals(TicketStatus.PENDING.name(), output.ticketStatus());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento que não existe")
    public void testReserveTicketWithoutEvent() throws Exception {
        //given
        final var customerID = TSID.fast().toLong();
        final var eventID = TSID.fast().toLong();
        final var expectedError = "Event not found";

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(eventID, customerID);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(customerService.findById(customerID)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventID)).thenReturn(Optional.empty());

        final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar um ticket com um cliente não existente")
    public void testReserveTicketWithoutCustomer() throws Exception {
        //given
        final var customerID = TSID.fast().toLong();
        final var eventID = TSID.fast().toLong();
        final var expectedError = "Customer not found";

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(eventID, customerID);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(customerService.findById(customerID)).thenReturn(Optional.empty());

        final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Um mesmo cliente não pode comprar mais de um ticket por evento")
    public void testReserveTicketMoreThanOnce() throws Exception {
        //given
        final var customerID = TSID.fast().toLong();
        final var eventID = TSID.fast().toLong();
        final var expectedError = "Ticket already registered";

        final var aEvent = new Event();
        aEvent.setId(eventID);
        aEvent.setName("Disney");
        aEvent.setTotalSpots(10);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(eventID, customerID);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(customerService.findById(customerID)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventID)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventID, customerID)).thenReturn(Optional.of(new Ticket()));

        final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Um mesmo cliente não pode comprar de um evento que não há mais lugares")
    public void testReserveTicketWithoutSpots() throws Exception {
        //given
        final var customerID = TSID.fast().toLong();
        final var eventID = TSID.fast().toLong();
        final var expectedError = "Event sold out";

        final var aEvent = new Event();
        aEvent.setId(eventID);
        aEvent.setName("Disney");
        aEvent.setTotalSpots(0);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(eventID, customerID);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(customerService.findById(customerID)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventID)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventID, customerID)).thenReturn(Optional.empty());

        final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
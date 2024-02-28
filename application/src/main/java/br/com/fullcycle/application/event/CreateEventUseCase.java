package br.com.fullcycle.application.event;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.partner.PartnerRepository;

import java.util.Objects;

public class CreateEventUseCase extends UseCase<CreateEventUseCase.Input, CreateEventUseCase.Output> {

    private final PartnerRepository partnerRepository;

    private final EventRepository eventRepository;

    public CreateEventUseCase(final PartnerRepository partnerRepository, final EventRepository eventRepository) {
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
        this.eventRepository = Objects.requireNonNull(eventRepository);
    }

    @Override
    public CreateEventUseCase.Output execute(CreateEventUseCase.Input input) {
        var partner = partnerRepository.partnerOfId(PartnerId.with(input.partnerId))
                .orElseThrow(() -> new ValidationException("Partner not found"));

        var event = eventRepository.create(Event.newEvent(input.name, input.date, input.totalSpots, partner));

        return new CreateEventUseCase.Output(event.getEventId().value(), input.date, event.getName().value(), input.totalSpots, input.partnerId);
    }

    public record Input(String date, String name, String partnerId, Integer totalSpots) {}
    public record Output(String id, String date, String name, Integer totalSpots, String partnerId) {}
}

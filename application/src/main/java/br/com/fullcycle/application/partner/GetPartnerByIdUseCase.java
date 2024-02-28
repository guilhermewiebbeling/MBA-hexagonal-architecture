package br.com.fullcycle.application.partner;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.partner.PartnerRepository;

import java.util.Objects;
import java.util.Optional;

public class GetPartnerByIdUseCase extends UseCase<GetPartnerByIdUseCase.Input, Optional<GetPartnerByIdUseCase.Output>> {

    private final PartnerRepository partnerRepository;

    public GetPartnerByIdUseCase(final PartnerRepository partnerRepository) {
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
    }

    @Override
    public Optional<Output> execute(Input input) {
        return partnerRepository.partnerOfId(PartnerId.with(input.id))
                .map(c -> new GetPartnerByIdUseCase.Output(c.getPartnerId().value(), c.getCnpj().value(), c.getEmail().value(), c.getName().value()));
    }

    public record Input(String id) {}
    public record Output(String id, String cnpj, String email, String name) {}
}    


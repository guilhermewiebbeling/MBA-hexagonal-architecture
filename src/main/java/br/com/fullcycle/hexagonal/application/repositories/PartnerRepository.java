package br.com.fullcycle.hexagonal.application.repositories;

import br.com.fullcycle.hexagonal.application.domain.Partner;
import br.com.fullcycle.hexagonal.application.domain.PartnerId;

import java.util.Optional;

public interface PartnerRepository {

    Optional<Partner> partnerOfId(PartnerId id);

    Optional<Partner> partnerOfCnpj(String cnpj);

    Optional<Partner> partnerOfEmail(String email);

    Partner create(Partner partner);

    Partner update (Partner partner);
}

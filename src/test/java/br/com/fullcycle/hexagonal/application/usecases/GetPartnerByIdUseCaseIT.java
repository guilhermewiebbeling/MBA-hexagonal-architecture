/*package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.repositories.PartnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class GetPartnerByIdUseCaseIT extends IntegrationTest {

    @Autowired
    private GetPartnerByIdUseCase useCase;

    @Autowired
    private PartnerRepository partnerRepository;

    @BeforeEach
    void tearDown() {
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve obter um parceiro por id")
    public void testGetById() {
        //given
        final var expectedCNPJ = "41536538000100";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var partner = createPartner(expectedCNPJ, expectedEmail, expectedName);
        final var expectedID = partner.getId();

        final var aPartner = new Partner();
        aPartner.setId(expectedID);
        aPartner.setCnpj(expectedCNPJ);
        aPartner.setEmail(expectedEmail);
        aPartner.setName(expectedName);

        final var input = new GetPartnerByIdUseCase.Input(expectedID);
        //when
        final var output = useCase.execute(input).get();

        //then
        Assertions.assertEquals(expectedID, output.id());
        Assertions.assertEquals(expectedCNPJ, output.cnpj());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um parceiro não existente")
    public void testGetByIdWithInvalidId() throws Exception {
        //given
        final var expectedID = UUID.randomUUID().getMostSignificantBits();

        final var input = new GetPartnerByIdUseCase.Input(expectedID);
        //when
        final var output = useCase.execute(input);

        //then
        Assertions.assertTrue(output.isEmpty());
    }

    private Partner createPartner(final String cnpj, final String email, final String name) {
        final var aPartner = new Partner();
        aPartner.setCnpj(cnpj);
        aPartner.setEmail(email);
        aPartner.setName(name);
        return partnerRepository.save(aPartner);
    }
}*/
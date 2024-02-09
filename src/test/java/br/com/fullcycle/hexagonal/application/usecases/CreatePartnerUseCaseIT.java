package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.repositories.PartnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreatePartnerUseCaseIT extends IntegrationTest {

    @Autowired
    private CreatePartnerUseCase useCase;

    @Autowired
    private PartnerRepository partnerRepository;

    @BeforeEach
    void tearDown() {
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreate() {
        //given
        final var expectedCNPJ = "41536538000100";
        final var expectedEmail = "john.dutton@gmail.com";
        final var expectedName = "John Dutton";

        final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);
        //when
        final var output = useCase.execute(createInput);

        //then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCNPJ, output.cnpj());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
    public void testCreateWithDuplicatedCNPJShouldFail() throws Exception {
        //given
        final var expectedCNPJ = "41536538000100";
        final var expectedEmail = "john.dutton@gmail.com";
        final var expectedName = "John Dutton";;
        final var expectedError = "Partner already exists";

        createPartner(expectedCNPJ, expectedEmail, expectedName);

        final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);
        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        //given
        final var expectedCNPJ = "41536538000100";
        final var expectedEmail = "john.dutton@gmail.com";
        final var expectedName = "John Dutton";
        final var expectedError = "Partner already exists";

        createPartner("415365380001001", expectedEmail, expectedName);

        final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private Partner createPartner(final String cnpj, final String email, final String name) {
        final var aPartner = new Partner();
        aPartner.setCnpj(cnpj);
        aPartner.setEmail(email);
        aPartner.setName(name);
        return partnerRepository.save(aPartner);
    }
}

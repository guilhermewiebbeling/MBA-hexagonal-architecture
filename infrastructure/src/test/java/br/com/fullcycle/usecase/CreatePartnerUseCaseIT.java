package br.com.fullcycle.usecase;

import br.com.fullcycle.IntegrationTest;
import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerRepository;
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
    void setUp() {
        partnerRepository.deleteAll();;
    }

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreate() {
        //given
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

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
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";;
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
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        createPartner("41.536.538/0001-00", expectedEmail, expectedName);

        final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private Partner createPartner(final String cnpj, final String email, final String name) {
        return partnerRepository.create(Partner.newPartner(name, cnpj, email));
    }
}

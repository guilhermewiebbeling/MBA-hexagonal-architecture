package br.com.fullcycle.hexagonal.application.domain.partner;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PartnerTest {

    @Test
    @DisplayName("Deve instanciar um parceiro")
    public void testCreate() {
        //given
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

       //when
        final var actualPartner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

        //then
        Assertions.assertNotNull(actualPartner.getPartnerId());
        Assertions.assertEquals(expectedCNPJ, actualPartner.getCnpj().value());
        Assertions.assertEquals(expectedEmail, actualPartner.getEmail().value());
        Assertions.assertEquals(expectedName, actualPartner.getName().value());
    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com CNPJ inválido")
    public void testCreatePartnerWithInvalidCnpj() {
        //given
        final var expectedException = "Invalid value for Cnpj";

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> Partner.newPartner("John Doe", "41536.538/0001-00", "john.doe@gmail.com"));

        //then
        Assertions.assertEquals(expectedException, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com nome inválido")
    public void testCreatePartnerWithInvalidName() {
        //given
        final var expectedException = "Invalid value for Name";

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> Partner.newPartner(null, "123.456.789-01", "john.doe@gmail.com"));

        //then
        Assertions.assertEquals(expectedException, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com email inválido")
    public void testCreatePartnerWithInvalidEmail() {
        //given
        final var expectedException = "Invalid value for Email";

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail"));

        //then
        Assertions.assertEquals(expectedException, actualException.getMessage());
    }
}

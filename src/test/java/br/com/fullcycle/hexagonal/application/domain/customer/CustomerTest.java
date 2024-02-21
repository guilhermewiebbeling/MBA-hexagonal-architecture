package br.com.fullcycle.hexagonal.application.domain.customer;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    @DisplayName("Deve instanciar um cliente")
    public void testCreate() {
        //given
        final var expectedCPF = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

       //when
        final var actualCustomer = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);

        //then
        Assertions.assertNotNull(actualCustomer.getCustomerId());
        Assertions.assertEquals(expectedCPF, actualCustomer.getCpf().value());
        Assertions.assertEquals(expectedEmail, actualCustomer.getEmail().value());
        Assertions.assertEquals(expectedName, actualCustomer.getName().value());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com CPF inválido")
    public void testCreateCustomerWithInvalidCpf() {
        //given
        final var expectedException = "Invalid value for Cpf";

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> Customer.newCustomer("John Doe", "123456.789-01", "john.doe@gmail.com"));

        //then
        Assertions.assertEquals(expectedException, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com nome inválido")
    public void testCreateCustomerWithInvalidName() {
        //given
        final var expectedException = "Invalid value for Name";

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> Customer.newCustomer(null, "123.456.789-01", "john.doe@gmail.com"));

        //then
        Assertions.assertEquals(expectedException, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com email inválido")
    public void testCreateCustomerWithInvalidEmail() {
        //given
        final var expectedException = "Invalid value for Email";

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail"));

        //then
        Assertions.assertEquals(expectedException, actualException.getMessage());
    }
}

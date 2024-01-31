package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CreateCustomerUseCaseTest {

    @Test
    @DisplayName("Deve criar um cliente")
    public void testCreate() {
        //given
        final var expectedCPF = "123456789";
        final var expectedEmail = "john.dutton@gmail.com";
        final var expectedName = "John Dutton";

        final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

        //when
        final var customerSerice = Mockito.mock(CustomerService.class);
        when(customerSerice.findByCpf(expectedCPF)).thenReturn(Optional.empty());
        when(customerSerice.findByEmail(expectedEmail)).thenReturn(Optional.empty());
        when(customerSerice.save(any())).thenAnswer(a -> {
            var customer = a.getArgument(0, Customer.class);
            customer.setId(UUID.randomUUID().getMostSignificantBits());
            return customer;
        });
        final var useCase = new CreateCustomerUseCase(customerSerice);
        final var output = useCase.execute(createInput);

        //then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCPF, output.cpf());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() throws Exception {
        //given
        final var expectedCPF = "123456789";
        final var expectedEmail = "john.dutton@gmail.com";
        final var expectedName = "John Dutton";
        final var expectedError = "Customer already exists";

        final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);
        final var aCustomer = new Customer();
        aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
        aCustomer.setCpf(expectedCPF);
        aCustomer.setEmail(expectedEmail);
        aCustomer.setName(expectedName);

        //when
        final var customerSerice = Mockito.mock(CustomerService.class);
        when(customerSerice.findByCpf(expectedCPF)).thenReturn(Optional.of(aCustomer));
        final var useCase = new CreateCustomerUseCase(customerSerice);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        //given
        final var expectedCPF = "123456789";
        final var expectedEmail = "john.dutton@gmail.com";
        final var expectedName = "John Dutton";
        final var expectedError = "Customer already exists";

        final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);
        final var aCustomer = new Customer();
        aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
        aCustomer.setCpf(expectedCPF);
        aCustomer.setEmail(expectedEmail);
        aCustomer.setName(expectedName);

        //when
        final var customerSerice = Mockito.mock(CustomerService.class);
        when(customerSerice.findByEmail(expectedEmail)).thenReturn(Optional.of(aCustomer));
        final var useCase = new CreateCustomerUseCase(customerSerice);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

class GetCustomerByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() {
        //given
        final var expectedID = UUID.randomUUID().getMostSignificantBits();
        final var expectedCPF = "123456789";
        final var expectedEmail = "john.dutton@gmail.com";
        final var expectedName = "John Dutton";

        final var aCustomer = new Customer();
        aCustomer.setId(expectedID);
        aCustomer.setCpf(expectedCPF);
        aCustomer.setEmail(expectedEmail);
        aCustomer.setName(expectedName);

        final var input = new GetCustomerByIdUseCase.Input(expectedID);
        //when
        final var customerSerice = Mockito.mock(CustomerService.class);
        when(customerSerice.findById(expectedID)).thenReturn(Optional.of(aCustomer));

        final var useCase = new GetCustomerByIdUseCase(customerSerice);
        final var output = useCase.execute(input).get();

        //then
        Assertions.assertEquals(expectedID, output.id());
        Assertions.assertEquals(expectedCPF, output.cpf());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um cliente não existente")
    public void testGetByIdWithInvalidId() throws Exception {
        //given
        final var expectedID = UUID.randomUUID().getMostSignificantBits();

        final var input = new GetCustomerByIdUseCase.Input(expectedID);
        //when
        final var customerSerice = Mockito.mock(CustomerService.class);
        when(customerSerice.findById(expectedID)).thenReturn(Optional.empty());

        final var useCase = new GetCustomerByIdUseCase(customerSerice);
        final var output = useCase.execute(input);

        //then
        Assertions.assertTrue(output.isEmpty());
    }
}
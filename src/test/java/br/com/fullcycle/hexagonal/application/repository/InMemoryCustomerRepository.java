package br.com.fullcycle.hexagonal.application.repository;

import br.com.fullcycle.hexagonal.application.domain.Customer;
import br.com.fullcycle.hexagonal.application.domain.CustomerId;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> customers;
    private final Map<String, Customer> customersByCPF;
    private final Map<String, Customer> customersByEmail;

    public InMemoryCustomerRepository() {
        this.customers = new HashMap<>();
        this.customersByCPF = new HashMap<>();
        this.customersByEmail = new HashMap<>();
    }

    @Override
    public Optional<Customer> customerOfId(CustomerId anId) {
        return Optional.ofNullable(this.customers.get(Objects.requireNonNull(anId).value()));
    }

    @Override
    public Optional<Customer> customerOfCpf(String cpf) {
        return Optional.ofNullable(this.customersByCPF.get(cpf));
    }

    @Override
    public Optional<Customer> customerOfEmail(String email) {
        return Optional.ofNullable(this.customersByEmail.get(email));
    }

    @Override
    public Customer create(Customer customer) {
        this.customers.put(customer.getCustomerId().value(), customer);
        this.customersByCPF.put(customer.getCpf().value(), customer);
        this.customersByEmail.put(customer.getEmail().value(), customer);
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        this.customers.put(customer.getCustomerId().value(), customer);
        this.customersByCPF.put(customer.getCpf().value(), customer);
        this.customersByEmail.put(customer.getEmail().value(), customer);
        return customer;
    }

}
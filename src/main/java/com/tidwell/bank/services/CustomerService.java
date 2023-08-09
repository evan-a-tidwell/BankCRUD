package com.tidwell.bank.services;

import com.tidwell.bank.models.Customer;
import com.tidwell.bank.repositories.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public List<Customer> getCustomers() {
        return customerRepo.findAll();
    }

    public Optional<Customer> getCustomer(Long id) {
        return customerRepo.findById(id);
    }

    public Customer create(Customer customer) {
        Optional<Customer> existing = customerRepo.findCustomerByEmail(customer.getEmail());
        if (existing.isPresent()) {
            throw new IllegalStateException("An account already exists with this email.");
        }

        return customerRepo.save(customer);
    }

    @Transactional
    public Customer update(Long currentCustomerId, Customer newCustomer) {
        Customer customer = customerRepo.findById(currentCustomerId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Customer with ID %1$d does not exist in the database.", currentCustomerId)
                ));
        customer.setFirstName(newCustomer.getFirstName());
        customer.setLastName(newCustomer.getLastName());
        customer.setEmail(newCustomer.getEmail());

        return customerRepo.save(customer);
    }

    public void delete(Long id) {
        customerRepo.deleteById(id);
    }
}

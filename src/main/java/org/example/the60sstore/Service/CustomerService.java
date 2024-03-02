package org.example.the60sstore.Service;

import jakarta.transaction.Transactional;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Repository.CustomerRepository;
import org.example.the60sstore.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, RoleRepository roleRepository) {
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Customer createCustomer(String firstName, String lastName, String birthDate, String email,
                                   String address, String username, String password, String role) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setDateOfBirth(LocalDate.parse(birthDate).atStartOfDay());
        customer.setEmail(email);
        customer.setAddress(address);
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setRegistrationDate(LocalDateTime.now());
        customer.setAccessCount(1);
        customer.setConfirmationStatus(false);
        customer.setRole(roleRepository.getRoleByRolename(role));
        return customerRepository.save(customer);
    }

    @Transactional
    public void incrementAccessCount(int customerId) {
        customerRepository.incrementAccessCount(customerId);
    }


    @Transactional
    public void confirmCustomer(int customerId) {
        customerRepository.updateConfirmationStatusById(customerId, true);
    }
}

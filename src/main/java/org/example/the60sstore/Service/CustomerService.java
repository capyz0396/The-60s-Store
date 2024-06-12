package org.example.the60sstore.Service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.CustomerLevel;
import org.example.the60sstore.Repository.CustomerRepository;
import org.example.the60sstore.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    public Customer createCustomer(String firstName, String lastName, String birthDate, String phoneNumber,
                                   String email, String address, String username, String password, String role) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setDateOfBirth(LocalDate.parse(birthDate).atStartOfDay());
        customer.setPhoneNumber(phoneNumber);
        customer.setEmail(email);
        customer.setAddress(address);
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setRegistrationDate(LocalDateTime.now());
        customer.setAccessCount(1);
        customer.setConfirmationStatus(false);
        customer.setRole(roleRepository.getRoleByRolename(role));
        customer.setLoyaltyPoint(0);
        CustomerLevel customerLevel = new CustomerLevel();
        customerLevel.setLevelId(1);
        customer.setCustomerLevel(customerLevel);
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

    public void addLogged(HttpSession session, Model model) {
        boolean logged = false;
        if (session.getAttribute("logged") != null) {
            logged = (boolean) session.getAttribute("logged");
        } else {
            session.setAttribute("logged", false);
        }

        model.addAttribute("logged", logged);
    }

    public Customer getCustomerByCustomerId(int customerId) {
        return customerRepository.getCustomerByCustomerId(customerId);
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.getCustomerByEmail(email);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByUsername(String username) {
        return customerRepository.getCustomerByUsername(username);
    }
}

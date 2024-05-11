package org.example.the60sstore.Security;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/* CustomUserDetailsService defines customerRepository to Spring Security get account. */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    /* It needs to create customerRepository to execute features. */
    @Autowired
    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /* loadUserByUsername will get the customer by customerRepository by username.
    * After that, return it like UserDetails. */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.getCustomerByUsername(username);
        if (customer != null) return customer;
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}

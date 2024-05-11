package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.CustomerLevel;
import org.example.the60sstore.Repository.CustomerLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/* CustomerLevelService returns CustomerLevel or list of them to Controller. */
@Service
public class CustomerLevelService {

    private final CustomerLevelRepository customerLevelRepository;

    /* Service always need to create Repository first. */
    public CustomerLevelService(CustomerLevelRepository customerLevelRepository) {
        this.customerLevelRepository = customerLevelRepository;
    }

    /* getAll method return list of all CustomerLevel in database. */
    public List<CustomerLevel> getAll() {
        return customerLevelRepository.findAll();
    }
}

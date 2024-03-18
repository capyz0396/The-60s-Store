package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.CustomerLevel;
import org.example.the60sstore.Repository.CustomerLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerLevelService {

    private final CustomerLevelRepository customerLevelRepository;

    public CustomerLevelService(CustomerLevelRepository customerLevelRepository) {
        this.customerLevelRepository = customerLevelRepository;
    }

    public List<CustomerLevel> getAll() {
        return customerLevelRepository.findAll();
    }
}

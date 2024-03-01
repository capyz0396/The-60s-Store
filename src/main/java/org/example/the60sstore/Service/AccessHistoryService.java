package org.example.testspring.Service;

import org.example.testspring.Entity.AccessHistory;
import org.example.testspring.Entity.Customer;
import org.example.testspring.Repository.AccessHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccessHistoryService {

    private final AccessHistoryRepository accessHistoryRepository;

    @Autowired
    public AccessHistoryService(AccessHistoryRepository accessHistoryRepository) {
        this.accessHistoryRepository = accessHistoryRepository;
    }


    public void logAccess(Customer customer) {
        AccessHistory accessHistory = new AccessHistory();
        accessHistory.setCustomer(customer);
        accessHistory.setAccessDate(LocalDateTime.now());
        accessHistoryRepository.save(accessHistory);
    }
}

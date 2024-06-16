package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.AccessHistory;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Repository.AccessHistoryRepository;
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

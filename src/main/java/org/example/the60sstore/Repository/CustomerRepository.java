package org.example.the60sstore.Repository;

import org.example.the60sstore.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Modifying
    @Query("UPDATE Customer c SET c.confirmationStatus = :confirmationStatus WHERE c.customerId = :customerId")
    void updateConfirmationStatusById(@Param("customerId") Integer customerId, @Param("confirmationStatus") boolean confirmationStatus);

    @Modifying
    @Query("UPDATE Customer c SET c.accessCount = c.accessCount + 1 WHERE c.customerId = ?1")
    void incrementAccessCount(int customerId);

    Customer getCustomerByUsername(String username);

    Customer getCustomerByCustomerId(int customerId);
}

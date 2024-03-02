package org.example.the60sstore.Repository;

import jakarta.transaction.Transactional;
import org.example.the60sstore.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE Token t SET t.isTokenConfirmed = true WHERE t.token = :tokenValue")
    void confirmTokenByTokenValue(@Param("tokenValue") String tokenValue);

    @Transactional
    @Modifying
    @Query("SELECT t FROM Token t WHERE t.customer.customerId = :customerId ORDER BY t.expiryDate DESC")
    List<Token> findLatestTokenByCustomerId(@Param("customerId") int customerId);
}

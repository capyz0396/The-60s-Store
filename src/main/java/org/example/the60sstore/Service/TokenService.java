package org.example.testspring.Service;

import org.example.testspring.Entity.Customer;
import org.example.testspring.Entity.Token;
import org.example.testspring.Repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void createToken(Customer customer, String generatedToken) {
        Token token = new Token();
        token.setCustomer(customer);
        token.setToken(generatedToken);
        token.setExpiryDate(calculateExpiryDate());
        tokenRepository.save(token);
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusDays(1);
    }

    public Token findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void updateConfirm(String token) {
        tokenRepository.confirmTokenByTokenValue(token);
    }

    public Token findNewestTokenForCustomer(Integer customerId) {
        return tokenRepository.findLatestTokenByCustomerId(customerId).getFirst();
    }
}

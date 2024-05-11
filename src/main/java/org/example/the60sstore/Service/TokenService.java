package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.Token;
import org.example.the60sstore.Repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/* TokenService returns Token or list of them to Controller. */
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    /* Service always need to create Repository first. */
    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /* createToken method create new token and set it by params, use token repository save to database. */
    public void createToken(Customer customer, String generatedToken) {
        Token token = new Token();
        token.setCustomer(customer);
        token.setToken(generatedToken);
        token.setExpiryDate(calculateExpiryDate());
        tokenRepository.save(token);
    }

    /* generateToken method returns 1 randomUUID. */
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    /* calculateExpiryDate returns LocalDateTime of 1 day later. */
    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusDays(1);
    }

    /* findByToken method uses token param to find Token object and return it. */
    public Token findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    /* updateConfirm method uses token to update itself to confirmed in database. */
    public void updateConfirm(String token) {
        tokenRepository.confirmTokenByTokenValue(token);
    }

    /* findNewestTokenForCustomer method gets last token by customerId. */
    public Token findNewestTokenForCustomer(Integer customerId) {
        return tokenRepository.findLatestTokenByCustomerId(customerId).getFirst();
    }
}

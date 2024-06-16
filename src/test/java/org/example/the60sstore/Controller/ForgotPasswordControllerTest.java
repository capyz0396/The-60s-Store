package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.Token;
import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.EmailSenderService;
import org.example.the60sstore.Service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ForgotPassword.class)
public class ForgotPasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private EmailSenderService emailSenderService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testToForgotPassword() throws Exception {
        mockMvc.perform(get("/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-forgot-password"));
    }

    @Test
    public void testCheckEmailAndSentToken() throws Exception {
        // Mocking behavior for customerService
        Customer mockedCustomer = new Customer();
        mockedCustomer.setEmail("test@example.com");
        when(customerService.getCustomerByEmail("test@example.com")).thenReturn(mockedCustomer);

        // Mocking behavior for tokenService
        Token mockedToken = new Token();
        mockedToken.setExpiryDate(LocalDateTime.now().plusDays(1)); // Assuming token is valid for 1 day
        when(tokenService.findNewestTokenForCustomer(any())).thenReturn(mockedToken);

        mockMvc.perform(get("/check-email").param("email", "test@example.com"))
                                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/google"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCheckEmailAndSentToken_CustomerNotFound() throws Exception {
        String email = "nonexistent@example.com";
        when(customerService.getCustomerByEmail(email)).thenReturn(null);

        mockMvc.perform(get("/check-email")
                        .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register-confirm-status?status=renew-password-denied"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testConfirmTokenRenewPassword_ValidToken() throws Exception {
        String token = "validToken";
        Customer customer = new Customer();
        Token validToken = new Token();
        validToken.setCustomer(customer);
        validToken.setToken(token);
        validToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        when(tokenService.findByToken(token)).thenReturn(validToken);

        mockMvc.perform(get("/check-token-renew-password")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(view().name("user-update-password"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testConfirmTokenRenewPassword_ExpiredToken() throws Exception {
        String token = "expiredToken";
        Customer customer = new Customer();
        Token expiredToken = new Token();
        expiredToken.setCustomer(customer);
        expiredToken.setToken(token);
        expiredToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        when(tokenService.findByToken(token)).thenReturn(expiredToken);

        mockMvc.perform(get("/check-token-renew-password")
                        .param("token", token))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testConfirmTokenRenewPassword_NullToken() throws Exception {
        String token = "nonexistentToken";
        when(tokenService.findByToken(token)).thenReturn(null);

        mockMvc.perform(get("/check-token-renew-password")
                        .param("token", token))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register-confirm-status?status=renew-password-attacked"));
    }
}

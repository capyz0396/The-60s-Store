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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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
        mockMvc.perform(MockMvcRequestBuilders.get("/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-forgot-password"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCheckEmailAndSentToken_Success() throws Exception {
        String email = "test@example.com";
        Customer customer = new Customer();
        customer.setEmail(email);
        when(customerService.getCustomerByEmail(email)).thenReturn(customer);
        doNothing().when(emailSenderService).sendEmail(anyString(), anyString(), anyString());

        mockMvc.perform(MockMvcRequestBuilders.get("/check-email")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(view().name("register-confirm"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCheckEmailAndSentToken_CustomerNotFound() throws Exception {
        String email = "nonexistent@example.com";
        when(customerService.getCustomerByEmail(email)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/check-email")
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

        mockMvc.perform(MockMvcRequestBuilders.get("/check-token-renew-password")
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

        mockMvc.perform(MockMvcRequestBuilders.get("/check-token-renew-password")
                        .param("token", token))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testConfirmTokenRenewPassword_NullToken() throws Exception {
        String token = "nonexistentToken";
        when(tokenService.findByToken(token)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/check-token-renew-password")
                        .param("token", token))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register-confirm-status?status=renew-password-attacked"));
    }
}

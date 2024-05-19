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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConfirmationController.class)
public class ConfirmationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private EmailSenderService emailSenderService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testConfirmRegistration_Agreed() throws Exception {
        Token token = new Token();
        token.setToken("valid_token");
        token.setExpiryDate(LocalDateTime.now().plusDays(1));

        Customer customer = new Customer();
        customer.setCustomerId(1);

        token.setCustomer(customer);

        doReturn(token).when(tokenService).findByToken("valid_token");
        doNothing().when(tokenService).updateConfirm("valid_token");

        mockMvc.perform(MockMvcRequestBuilders.get("/register-confirm?token=valid_token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register-confirm-status?status=agreed"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testConfirmRegistration_Expired() throws Exception {
        Token token = new Token();
        token.setToken("expired_token");
        token.setExpiryDate(LocalDateTime.now().minusDays(1));

        doReturn(token).when(tokenService).findByToken("expired_token");

        mockMvc.perform(MockMvcRequestBuilders.get("/register-confirm?token=expired_token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register-confirm-status?status=expired&token=expired_token"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testConfirmRegistration_Denied() throws Exception {
        doReturn(null).when(tokenService).findByToken("invalid_token");

        mockMvc.perform(MockMvcRequestBuilders.get("/register-confirm?token=invalid_token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register-confirm-status?status=denied"));
    }
}


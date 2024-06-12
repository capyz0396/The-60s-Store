package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SignupControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private AccessHistoryService accessHistoryService;

    @Mock
    private TokenService tokenService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private Model model;

    @InjectMocks
    private SignupController signupController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(signupController).build();
    }

    @Test
    public void testToSignup() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-signup"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    public void testProcessSignupForm_UserExists() throws Exception {
        when(customerService.getCustomerByUsername(anyString())).thenReturn(new Customer());

        mockMvc.perform(post("/signup")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("birthDate", "1990-01-01")
                        .param("phoneNumber", "091234567")
                        .param("email", "john.doe@example.com")
                        .param("address", "123 Street")
                        .param("username", "johndoe")
                        .param("password", "password")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-signup"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "This username is already in use"));

        verify(customerService).getCustomerByUsername(anyString());
    }

    @Test
    public void testProcessSignupForm_EmailExists() throws Exception {
        when(customerService.getCustomerByUsername(anyString())).thenReturn(null);
        when(customerService.getCustomerByEmail(anyString())).thenReturn(new Customer());

        mockMvc.perform(post("/signup")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("birthDate", "1990-01-01")
                        .param("phoneNumber", "091234567")
                        .param("email", "john.doe@example.com")
                        .param("address", "123 Street")
                        .param("username", "johndoe")
                        .param("password", "password")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-signup"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "This email is already in use"));

        verify(customerService).getCustomerByUsername(anyString());
        verify(customerService).getCustomerByEmail(anyString());
    }

    @Test
    public void testProcessSignupForm_Success() throws Exception {
        when(customerService.getCustomerByUsername(anyString())).thenReturn(null);
        when(customerService.getCustomerByEmail(anyString())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(customerService.createCustomer(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new Customer());

        mockMvc.perform(post("/signup")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("birthDate", "1990-01-01")
                        .param("phoneNumber", "091234567")
                        .param("email", "john.doe@example.com")
                        .param("address", "123 Street")
                        .param("username", "johndoe")
                        .param("password", "password")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-confirm"));

        verify(customerService).getCustomerByUsername(anyString());
        verify(customerService).getCustomerByEmail(anyString());
        verify(customerService).createCustomer(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        verify(accessHistoryService).logAccess(any(Customer.class));
        verify(emailSenderService).sendEmail(anyString(), anyString(), anyString());
    }
}

package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testProcessSignupForm_EmailExists() throws Exception {
        when(customerService.getCustomerByEmail(anyString())).thenReturn(new Customer());

        mockMvc.perform(post("/update-user-information")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("birthDate", "1990-01-01")
                        .param("email", "john.doe@example.com")
                        .param("address", "123 Street")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-information-edit"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "This email address is already in use."));

        verify(customerService).getCustomerByEmail("john.doe@example.com");
    }
}

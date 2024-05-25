package org.example.the60sstore.Controller;

import org.example.the60sstore.Controller.ManagerController;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.Role;
import org.example.the60sstore.Service.CartService;
import org.example.the60sstore.Service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ManagerControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private CustomerService customerService;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2AuthenticationToken oauthToken;

    @Mock
    private Model model;

    @InjectMocks
    private ManagerController managerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }

    @Test
    public void testToProfileAsAdmin() throws Exception {
        Customer customer = new Customer();
        Role role = new Role();
        role.setRolename("ADMIN");
        customer.setRole(role);

        when(authentication.getPrincipal()).thenReturn(customer);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-profile"));
    }

    @Test
    public void testToProfileAsUser() throws Exception {
        Customer customer = new Customer();
        Role role = new Role();
        role.setRolename("USER");
        customer.setRole(role);

        when(authentication.getPrincipal()).thenReturn(customer);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile"));
    }

    @Test
    public void testToProfileAsOwner() throws Exception {
        Customer customer = new Customer();
        Role role = new Role();
        role.setRolename("OWNER");
        customer.setRole(role);

        when(authentication.getPrincipal()).thenReturn(customer);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("owner-profile"));
    }

    @Test
    public void testToProfileAsOAuth2User() throws Exception {
        Customer customer = new Customer();
        Role role = new Role();
        role.setRolename("USER");
        customer.setRole(role);

        Map<String, Object> attributes = Map.of("email", "test@example.com");
        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email");
        when(oauthToken.getPrincipal()).thenReturn(oAuth2User);
        when(customerService.getCustomerByEmail("test@example.com")).thenReturn(customer);
        SecurityContextHolder.getContext().setAuthentication(oauthToken);

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile"));
    }

    @Test
    public void testToProfileWhenNoRole() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new Object());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }
}

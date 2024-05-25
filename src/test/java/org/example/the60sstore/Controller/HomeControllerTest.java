package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Controller.HomeController;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.CustomerLevel;
import org.example.the60sstore.Entity.Role;
import org.example.the60sstore.Service.AccessHistoryService;
import org.example.the60sstore.Service.CartService;
import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.LanguageService;
import org.example.the60sstore.Service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccessHistoryService accessHistoryService;

    @MockBean
    private CartService cartService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private LanguageService languageService;

    @MockBean
    private RoleService roleService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testToHome_Logged_In_User() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setEmail("test@example.com");
        when(customerService.getCustomerByEmail("test@example.com")).thenReturn(customer);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", "test@example.com");
        attributes.put("given_name", "John");
        attributes.put("family_name", "Doe");
        OAuth2AuthenticationToken authenticationToken = mock(OAuth2AuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(new OAuth2UserImpl(attributes));
        when(authenticationToken.isAuthenticated()).thenReturn(true);
        when(roleService.getRoleByRoleName("USER")).thenReturn(new Role());
        when(customerService.save(any(Customer.class))).thenReturn(customer);

        doAnswer(invocation -> {
            Model model = invocation.getArgument(1);
            model.addAttribute("logged", true);
            return null;
        }).when(customerService).addLogged(any(HttpSession.class), any(Model.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/home")
                        .param("logged", "true")
                        .session(session)
                        .principal(authenticationToken))
                .andExpect(status().isOk())
                .andExpect(view().name("store-home"))
                .andExpect(model().attributeExists("logged", "order", "update"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ANONYMOUS"})
    public void testToHome_Not_Logged_In_User() throws Exception {

        doAnswer(invocation -> {
            Model model = invocation.getArgument(1);
            model.addAttribute("logged", false);
            return null;
        }).when(customerService).addLogged(any(HttpSession.class), any(Model.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("store-home"))
                .andExpect(model().attributeExists("logged", "order", "update"));
    }


    private static class OAuth2UserImpl implements OAuth2User {
        private final Map<String, Object> attributes;

        public OAuth2UserImpl(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return this.attributes;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.emptyList();
        }

        @Override
        public String getName() {
            return null;
        }
    }
}

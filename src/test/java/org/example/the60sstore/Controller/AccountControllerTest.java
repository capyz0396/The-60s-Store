package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testToAccountManager() throws Exception {

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("John");
        customer.setLastName("Smith");
        customer.setEmail("john.smith@gmail.com");
        customer.setPassword("password");
        customer.setUsername("user");
        customer.setLockStatus(false);

        List<Customer> accounts = Arrays.asList(customer);
        doReturn(accounts).when(customerService).getAllCustomer();

        mockMvc.perform(get("/account-manager"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-account"))
                .andExpect(model().attribute("accounts", accounts));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testChangeStatus() throws Exception {

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("John");
        customer.setLastName("Smith");
        customer.setEmail("john.smith@gmail.com");
        customer.setPassword("password");
        customer.setUsername("user");
        customer.setLockStatus(false);
        doReturn(customer).when(customerService).getCustomerByCustomerId(1);
        doReturn(customer).when(customerService).save(any(Customer.class));

        mockMvc.perform(get("/change-status")
                        .param("customerId", "1")
                        .param("action", "Lock")
                        .header("referer", "/previous-page"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/previous-page"));

        verify(customerService).save(Mockito.argThat(c -> c.getCustomerId() == 1 && c.getLockStatus()));
    }
}
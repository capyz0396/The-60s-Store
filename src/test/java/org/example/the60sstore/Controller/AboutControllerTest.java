package org.example.the60sstore.Controller;

import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.LanguageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(AboutController.class)
public class AboutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private LanguageService languageService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testToAbout() throws Exception {
        doAnswer(invocation -> {
            Model model = invocation.getArgument(1);
            model.addAttribute("logged", true);
            return null;
        }).when(customerService).addLogged(any(HttpSession.class), any(Model.class));
        doNothing().when(languageService).addLanguagle(any(HttpServletRequest.class), any(Model.class));

        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("store-about"));
    }
}

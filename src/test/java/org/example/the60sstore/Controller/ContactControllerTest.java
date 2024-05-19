package org.example.the60sstore.Controller;

import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.EmailSenderService;
import org.example.the60sstore.Service.LanguageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
public class ContactControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private LanguageService languageService;

    @MockBean
    private EmailSenderService emailSenderService;

    /*@Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    public void testSentMessage_Success() throws Exception {

        doNothing().when(emailSenderService).sendEmail(anyString(), anyString(), anyString());
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(MockMvcRequestBuilders.post("/sent-message")
                        .param("name", "John Doe")
                        .param("email", "johndoe@example.com")
                        .param("subject", "Test Subject")
                        .param("message", "Test Message")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("store-home"))
                .andExpect(model().attributeExists("sent"))
                .andExpect(model().attribute("sent", "success"));
    }*/
}

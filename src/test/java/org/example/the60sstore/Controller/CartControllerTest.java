package org.example.the60sstore.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Entity.ProductPrice;
import org.example.the60sstore.Service.CartService;
import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.LanguageService;
import org.example.the60sstore.Service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private LanguageService languageService;

    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testRemoveOutCart() throws Exception {
        // Giả lập session với danh sách Product
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setQuantity(2);
        products.add(product);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", products);
        session.setAttribute("cartSize", 2);

        doNothing().when(languageService).addLanguagle(any(HttpServletRequest.class), any(Model.class));

        mockMvc.perform(get("/removeOutCart").param("productId", "1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:cart"))
                .andExpect(model().attributeExists("products"));

        List<Product> updatedCart = (List<Product>) session.getAttribute("cart");
        assert updatedCart != null;
        assert updatedCart.isEmpty();
        assert session.getAttribute("cartSize").equals(0);
    }
}

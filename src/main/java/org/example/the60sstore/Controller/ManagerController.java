package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.CartService;
import org.example.the60sstore.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/* ManagerController resolves features related profile page of types of account. */
@Controller
public class ManagerController {

    private final CartService cartService;
    private final CustomerService customerService;

    /* The controller only needs to create cartService to run. */
    @Autowired
    public ManagerController(CartService cartService, CustomerService customerService) {
        this.cartService = cartService;
        this.customerService = customerService;
    }

    /* toProfile method return page depend on role of accounts.
    * Account information get from SecurityContextHolder. */
    @GetMapping("/profile")
    public String toProfile(HttpSession session, Model model) {

        String role = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
            Customer customer = customerService.getCustomerByEmail(attributes.get("email").toString());
            role = "ROLE_" + customer.getRole().getRolename();
        }
        if (authentication.getPrincipal() instanceof Customer customer) {
            role = "ROLE_" + customer.getRole().getRolename();
        }
        
        cartService.addNumCart(session, model);

        switch (role) {
            case "ROLE_ADMIN" -> {
                return "admin-profile";
            }
            case "ROLE_USER" -> {
                return "user-profile";
            }
            case "ROLE_OWNER" -> {
                return "owner-profile";
            }
        }
        return "redirect:/home";
    }
}

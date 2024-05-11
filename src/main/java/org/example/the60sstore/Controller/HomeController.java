package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.CustomerLevel;
import org.example.the60sstore.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Map;

/* HomeController solves features at home.html. */
@Controller
public class HomeController {

    private final AccessHistoryService accessHistoryService;
    private final CartService cartService;
    private final CustomerService customerService;
    private final LanguageService languageService;
    private final RoleService roleService;

    /* HomeController always needs 4 services to show information on store-home.html. */
    @Autowired
    public HomeController(AccessHistoryService accessHistoryService, CustomerService customerService, CartService cartService, LanguageService languageService, RoleService roleService) {
        this.accessHistoryService = accessHistoryService;
        this.cartService = cartService;
        this.customerService = customerService;
        this.languageService = languageService;
        this.roleService = roleService;
    }

    /* toHome method set 2 url to access store-home.html.
     * When a customer logged, updated information or order successfully, the method will receive params.
     * These params are added to model to show on html. */
    @GetMapping({"/", "/home"})
    public String toHome(@RequestParam(name = "logged", defaultValue = "false") boolean logged,
                         @RequestParam(name = "order", defaultValue = "") String order,
                         @RequestParam(name = "update", defaultValue = "") String update,
                         Model model, HttpServletRequest request, HttpSession session) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
                Customer customer = customerService.getCustomerByEmail(attributes.get("email").toString());
                customer = (customer == null) ? createNewOAuth2Customer(oauthToken.getPrincipal().getName(), attributes) : customer;
                addAccessCountAndHistory(customer);
            }
            if (authentication.getPrincipal() instanceof Customer customer) {
                addAccessCountAndHistory(customer);
            }
        }

        setAttributesForModelAndSession(logged, order, update, model, session);
        customerService.addLogged(session, model);
        cartService.addNumCart(session, model);
        languageService.addLanguagle(request, model);

        return "store-home";
    }

    private static void setAttributesForModelAndSession(boolean logged, String order, String update, Model model, HttpSession session) {
        session.setAttribute("logged", logged ? true : session.getAttribute("logged"));
        model.addAttribute("order", order);
        model.addAttribute("update", update);
    }

    private void addAccessCountAndHistory(Customer customer) {
        customerService.incrementAccessCount(customer.getCustomerId());
        accessHistoryService.logAccess(customer);
    }

    private Customer createNewOAuth2Customer(String name, Map<String, Object> attributes) {

        Customer customer = new Customer();
        customer.setUsername(name);
        customer.setFirstName(attributes.get("given_name").toString());
        customer.setLastName(attributes.get("family_name").toString());
        customer.setEmail(attributes.get("email").toString());
        customer.setRegistrationDate(LocalDateTime.now());
        customer.setAccessCount(1);
        customer.setConfirmationStatus(true);
        customer.setRole(roleService.getRoleByRoleName("USER"));
        customer.setLoyaltyPoint(0);
        CustomerLevel customerLevel = new CustomerLevel();
        customerLevel.setLevelId(1);
        customer.setCustomerLevel(customerLevel);
        customer.setLockStatus(false);
        customerService.save(customer);

        return customer;
    }
}

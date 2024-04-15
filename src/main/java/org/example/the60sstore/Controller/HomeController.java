package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.AccessHistoryService;
import org.example.the60sstore.Service.CartService;
import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Controller
public class HomeController {

    private final AccessHistoryService accessHistoryService;
    private final CartService cartService;
    private final CustomerService customerService;
    private final LanguageService languageService;


    @Autowired
    public HomeController(AccessHistoryService accessHistoryService, CustomerService customerService, CartService cartService, LanguageService languageService) {
        this.accessHistoryService = accessHistoryService;
        this.cartService = cartService;
        this.customerService = customerService;
        this.languageService = languageService;
    }

    @GetMapping({"/", "/home"})
    public String defaultHome(@RequestParam(name = "logged", defaultValue = "false") boolean logged,
                              @RequestParam(name = "order", defaultValue = "") String order,
                              @RequestParam(name = "update", defaultValue = "") String update,
                              Model model,
                              HttpServletRequest request,
                              HttpSession session) {

        model.addAttribute("order", order);
        model.addAttribute("update", update);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (logged) {
            session.setAttribute("logged", true);
            Customer customer = (Customer) authentication.getPrincipal();
            customerService.incrementAccessCount(customer.getCustomerId());
            accessHistoryService.logAccess(customer);
        }
        customerService.addLogged(session, model);
        cartService.addNumCart(session, model);
        languageService.addLanguagle(request, model);

        return "store-home";
    }
}

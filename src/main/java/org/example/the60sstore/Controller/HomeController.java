package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.AccessHistoryService;
import org.example.the60sstore.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final CustomerService customerService;

    private final AccessHistoryService accessHistoryService;

    @Autowired
    public HomeController(CustomerService customerService, AccessHistoryService accessHistoryService) {
        this.customerService = customerService;
        this.accessHistoryService = accessHistoryService;
    }

    @GetMapping({"/", "/home"})
    public String defaultHome(@RequestParam(name = "logged", defaultValue = "false") boolean logged,
                              Model model, HttpSession session) {

        boolean alreadyLogged = false;

        if (session.getAttribute("logged") != null) {
             alreadyLogged = (boolean) session.getAttribute("logged");
        }

        if (logged) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Customer customer = (Customer) authentication.getPrincipal();
            customerService.incrementAccessCount(customer.getCustomerId());
            accessHistoryService.logAccess(customer);
            model.addAttribute("logged", true);
        } else if (alreadyLogged) {
            model.addAttribute("logged", true);
        } else {
            model.addAttribute("logged", false);
        }

        return "store-home";
    }
}

package org.example.testspring.Controller;

import org.example.testspring.Entity.Customer;
import org.example.testspring.Service.AccessHistoryService;
import org.example.testspring.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/home")
    public String home(@RequestParam(name = "logged", defaultValue = "false") boolean logged) {

        if (logged) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Customer customer = (Customer) authentication.getPrincipal();
            customerService.incrementAccessCount(customer.getCustomerId());
            accessHistoryService.logAccess(customer);
        }
        return "home";
    }
}

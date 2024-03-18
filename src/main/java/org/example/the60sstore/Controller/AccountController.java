package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AccountController {

    private final CustomerService customerService;

    public AccountController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/account-manager")
    public String toAccountManager(Model model) {

        List<Customer> accounts = customerService.getAllCustomer();

        model.addAttribute("accounts", accounts);
        return "admin-account";
    }
}

package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/* Controller executes features related Customer Entity. */
@Controller
public class AccountController {

    private final CustomerService customerService;

    /* This controller needs create CustomerService for all features. */
    public AccountController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /* toAccountManager change customer view to admin-account.html.
    * It adds account list from database. */
    @GetMapping("/account-manager")
    public String toAccountManager(Model model) {

        List<Customer> accounts = customerService.getAllCustomer();
        model.addAttribute("accounts", accounts);

        return "admin-account";
    }

    /* changeStatus method receives action and customerId to change status of account.
    * The "action" param received from client always contains "Lock" or "Unlock" of String.
    * After executing, redirect to last page. */
    @GetMapping("/change-status")
    public String changeStatus(@RequestParam("customerId") int customerId,
                               @RequestParam("action") String action,
                               HttpServletRequest request) {

        Customer customer = customerService.getCustomerByCustomerId(customerId);
        customer.setLockStatus(action.equals("Lock"));
        customerService.save(customer);
        String referer = request.getHeader("referer");

        return "redirect:" + referer;
    }
}

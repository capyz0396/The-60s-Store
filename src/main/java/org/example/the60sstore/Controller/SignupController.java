package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignupController {

    private final CustomerService customerService;
    private final AccessHistoryService accessHistoryService;
    private final TokenService tokenService;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SignupController(
            CustomerService customerService,
            AccessHistoryService accessHistoryService,
            TokenService tokenService,
            EmailSenderService emailSenderService,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.customerService = customerService;
        this.accessHistoryService = accessHistoryService;
        this.tokenService = tokenService;
        this.emailSenderService = emailSenderService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("customer", new Customer());
        return "user-signup";
    }

    @GetMapping("/admin-signup")
    public String adminSignup(Model model) {
        model.addAttribute("customer", new Customer());
        return "admin-signup";
    }

    @PostMapping("/signup")
    public String processSignupForm(@RequestParam String firstName, @RequestParam String lastName,
                                    @RequestParam String birthDate,
                                    @RequestParam String email, @RequestParam String address,
                                    @RequestParam String username, @RequestParam String password,
                                    @RequestParam String role, Model model) {

        Customer customer = customerService.getCustomerByUsername(username);
        if (customer != null) {
            model.addAttribute("error", "This username is already in use");
            return "user-signup";
        }
        customer = customerService.getCustomerByEmail(email);
        if (customer != null) {
            model.addAttribute("error", "This email is already in use");
            return "user-signup";
        }

        String hashedPassword = bCryptPasswordEncoder.encode(password);
        Customer newCustomer = customerService.createCustomer(firstName, lastName, birthDate, email, address, username, hashedPassword, role);
        newCustomer.setLockStatus(false);
        String token = tokenService.generateToken();
        accessHistoryService.logAccess(newCustomer);
        tokenService.createToken(newCustomer, token);
        emailSenderService.sendEmail(email, "Welcome to my factory!", "Here is your token:\n http://localhost:8080/register-confirm?token=" + token);

        return "register-confirm";
    }

    @PostMapping("/reconfirm")
    public String confirmRegistration(@RequestParam("token") String token) {

        Customer customer = tokenService.findByToken(token).getCustomer();

        if (token.equals(tokenService.findNewestTokenForCustomer(customer.getCustomerId()).getToken())) {
            token = tokenService.generateToken();
            tokenService.createToken(customer, token);
            emailSenderService.sendEmail(customer.getEmail(), "Welcome to my factory!", "Here is your token:\n http://localhost:8080/confirm?token=" + token);
            return "register-confirm";
        } else {
            return "redirect:/register-confirm-status?status=renew-denied";
        }
    }
}

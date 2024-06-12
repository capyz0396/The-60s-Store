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

/* SignupController resolves features related to sign up page.  */
@Controller
public class SignupController {

    private final CustomerService customerService;
    private final AccessHistoryService accessHistoryService;
    private final TokenService tokenService;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /* To execute methods in it, services are created in here. */
    @Autowired
    public SignupController(CustomerService customerService,
            AccessHistoryService accessHistoryService,
            TokenService tokenService,
            EmailSenderService emailSenderService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerService = customerService;
        this.accessHistoryService = accessHistoryService;
        this.tokenService = tokenService;
        this.emailSenderService = emailSenderService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /* toSignup method sets url /signup for user-signup.html. */
    @GetMapping("/signup")
    public String toSignup(Model model) {
        model.addAttribute("customer", new Customer());
        return "user-signup";
    }

    /* toAdminSignup method sets url /signup for admin-signup.html. */
    @GetMapping("/admin-signup")
    public String toAdminSignup(Model model) {
        model.addAttribute("customer", new Customer());
        return "admin-signup";
    }

    /* Admin and user also use processSignupForm to create new account.
    * If username or email is exist, return to last page and show notification.
    * Else, hash password by bCryptPasswordEncoder and create new customer.
    * Create new history log.
    * Create new token and send it to customer's email. */
    @PostMapping("/signup")
    public String processSignupForm(@RequestParam String firstName, @RequestParam String lastName,
                                    @RequestParam String birthDate, @RequestParam String phoneNumber,
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
        Customer newCustomer = customerService.createCustomer(firstName, lastName, birthDate, phoneNumber, email, address, username, hashedPassword, role);
        newCustomer.setLockStatus(false);
        String token = tokenService.generateToken();
        accessHistoryService.logAccess(newCustomer);
        tokenService.createToken(newCustomer, token);
        emailSenderService.sendEmail(email, "Welcome to my factory!", "Here is your token:\n http://localhost:8080/register-confirm?token=" + token);

        return "register-confirm";
    }
}

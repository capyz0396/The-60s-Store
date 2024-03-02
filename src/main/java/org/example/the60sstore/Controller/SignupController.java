package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.Role;
import org.example.the60sstore.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignupController {

    private final CustomerService customerService;
    private final AccessHistoryService accessHistoryService;
    private final TokenService tokenService;
    private final RoleService roleService;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SignupController(
            CustomerService customerService,
            AccessHistoryService accessHistoryService,
            TokenService tokenService,
            RoleService roleService,
            EmailSenderService emailSenderService,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.customerService = customerService;
        this.accessHistoryService = accessHistoryService;
        this.tokenService = tokenService;
        this.roleService = roleService;
        this.emailSenderService = emailSenderService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignupForm(@RequestParam String firstName, @RequestParam String lastName,
                                    @RequestParam String birthDate,
                                    @RequestParam String email, @RequestParam String address,
                                    @RequestParam String username, @RequestParam String password,
                                    @RequestParam String role) {

        String hashedPassword = bCryptPasswordEncoder.encode(password);

        Customer newCustomer = customerService.createCustomer(firstName, lastName, birthDate, email, address, username, hashedPassword, role);
        String token = tokenService.generateToken();
        accessHistoryService.logAccess(newCustomer);
        tokenService.createToken(newCustomer, token);
        emailSenderService.sendEmail(email, "Welcome to my factory!", "Here is your token:\n http://localhost:8080/confirm?token=" + token);

        return "alert_confirm";
    }

    @PostMapping("/reconfirm")
    public String confirmRegistration(@RequestParam("token") String token) {

        Customer customer = tokenService.findByToken(token).getCustomer();

        if (token.equals(tokenService.findNewestTokenForCustomer(customer.getCustomerId()).getToken())) {
            token = tokenService.generateToken();
            tokenService.createToken(customer, token);
            emailSenderService.sendEmail(customer.getEmail(), "Welcome to my factory!", "Here is your token:\n http://localhost:8080/confirm?token=" + token);
            return "alert_confirm";
        } else {
            return "redirect:/confirmation-status?status=renew-denied";
        }
    }


    @GetMapping("/test")
    public String test() {
        Role role = roleService.getRoleByRoleName("ADMIN");
        if (role == null) {
            System.out.println("NULL");
        } else {
            System.out.println(role.getRoleid());
        }
        return "signup";
    }
}

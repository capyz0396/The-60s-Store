package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.Token;
import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.EmailSenderService;
import org.example.the60sstore.Service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/* Controller executes providing token and confirming from client. */
@Controller
public class ConfirmationController {

    private final TokenService tokenService;
    private final CustomerService customerService;
    private final EmailSenderService emailSenderService;

    /* To create and confirm user's status, the controller needs to create tokenService and customerServices. */
    @Autowired
    public ConfirmationController(TokenService tokenService, CustomerService customerService, EmailSenderService emailSenderService) {
        this.tokenService = tokenService;
        this.customerService = customerService;
        this.emailSenderService = emailSenderService;
    }

    /* This url receive "token" param and check it with database.
    * 3 situations will happen, token expired, token agreed, token denied. */
    @GetMapping("/register-confirm")
    public String confirmRegistration(@RequestParam("token") String token) {

        Token customerToken = tokenService.findByToken(token);

        if (customerToken != null && customerToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            customerService.confirmCustomer(customerToken.getCustomer().getCustomerId());
            tokenService.updateConfirm(customerToken.getToken());
            return "redirect:/register-confirm-status?status=agreed";
        } else if (customerToken != null && !customerToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            return "redirect:/register-confirm-status?status=expired&token=" + token;
        } else {
            return "redirect:/register-confirm-status?status=denied";
        }
    }

    /* confirmRegistration receives token when a customer click confirm link in mail.
     * The method will check token with database and create new token for customer. */
    @PostMapping("/reconfirm")
    public String reconfirmRegistration(@RequestParam("token") String token) {

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

    /* Transfer customer to register-confirm-status to show result. */
    @GetMapping("/register-confirm-status")
    public String confirmationStatus(@RequestParam (name = "status", required = false) String status,
                                     @RequestParam (name = "token", required = false) String token, Model model) {
        model.addAttribute("status", status);
        model.addAttribute("token", token);
        return "register-confirm-status";
    }
}

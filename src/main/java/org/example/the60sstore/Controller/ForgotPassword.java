package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.Token;
import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.EmailSenderService;
import org.example.the60sstore.Service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class ForgotPassword {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomerService customerService;
    private final EmailSenderService emailSenderService;
    private final TokenService tokenService;

    public ForgotPassword(BCryptPasswordEncoder bCryptPasswordEncoder, CustomerService customerService, EmailSenderService emailSenderService, TokenService tokenService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customerService = customerService;
        this.emailSenderService = emailSenderService;
        this.tokenService = tokenService;
    }

    @GetMapping({"forgot-password"})
    public String toForgotPassword() {
        return "user-forgot-password";
    }


    @GetMapping("check-email")
    public String checkEmailAndSentToken(@RequestParam(name = "email") String email) {

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer != null) {
            String token = tokenService.generateToken();
            tokenService.createToken(customer, token);
            emailSenderService.sendEmail(email, "Welcome to my factory!", "Here is your token:\n http://localhost:8080/check-token-renew-password?token=" + token);
            return "register-confirm";
        } else {
            return "redirect:/register-confirm-status?status=renew-password-denied";
        }
    }

    @GetMapping("check-token-renew-password")
    public String check(@RequestParam(name = "token") String token, Model model) {

        Token customerToken = tokenService.findByToken(token);

        if (customerToken != null && customerToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            model.addAttribute("user", customerToken.getCustomer().getCustomerId());
            return "user-update-password";
        } else if (customerToken != null && !customerToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            return "redirect:/register-confirm-status?status=renew-expired&token=" + token;
        } else {
            return "redirect:/register-confirm-status?status=renew-password-attacked";
        }
    }

    @PostMapping("/reconfirm-password")
    public String confirmRegistration(@RequestParam("token") String token) {

        Customer customer = tokenService.findByToken(token).getCustomer();

        if (token.equals(tokenService.findNewestTokenForCustomer(customer.getCustomerId()).getToken())) {
            token = tokenService.generateToken();
            tokenService.createToken(customer, token);
            emailSenderService.sendEmail(customer.getEmail(), "Welcome to my factory!", "Here is your token:\n http://localhost:8080/check-token-renew-password?token=" + token);
            return "register-confirm";
        } else {
            return "redirect:/register-confirm-status?status=renew-password-denied";
        }
    }

    @PostMapping("update-new-password")
    public String updateNewPassword(@RequestParam(name = "password") String password,
                                    @RequestParam(name = "userId") int userId) {

        Customer customer = customerService.getCustomerByCustomerId(userId);
        customer.setPassword(bCryptPasswordEncoder.encode(password));
        customerService.save(customer);

        return "redirect:/register-confirm-status?status=renew-password-agreed";
    }
}

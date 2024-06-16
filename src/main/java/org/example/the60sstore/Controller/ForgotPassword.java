package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.Token;
import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.EmailSenderService;
import org.example.the60sstore.Service.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/* Controller provides many methods to solve forget password situations. */
@Controller
public class ForgotPassword {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomerService customerService;
    private final EmailSenderService emailSenderService;
    private final TokenService tokenService;

    /* To solve them, the controller needs to create 4 services. */
    public ForgotPassword(BCryptPasswordEncoder bCryptPasswordEncoder, CustomerService customerService, EmailSenderService emailSenderService, TokenService tokenService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customerService = customerService;
        this.emailSenderService = emailSenderService;
        this.tokenService = tokenService;
    }

    /* Set url "forgot-password" for "user-forgot-password.html". */
    @GetMapping({"forgot-password"})
    public String toForgotPassword() {
        return "user-forgot-password";
    }

    /* checkEmailAndSentToken method checks customer having email from param.
    * If customer is not null, the method creates new token and send link to that mail.
    * Else showing notification "denied". */
    @GetMapping("check-email")
    public String checkEmailAndSentToken(@RequestParam(name = "email") String email) {

        Customer customer = customerService.getCustomerByEmail(email);

        if (customer != null) {
            /* Kiểm tra xem customer có token nào đang tồn tại k
            * Nếu tn tại thi khong gui nua.*/
            Token customerToken = tokenService.findNewestTokenForCustomer(customer.getCustomerId());
            if (!customerToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                String token = tokenService.generateToken();
                tokenService.createToken(customer, token);
                emailSenderService.sendEmail(email, "Welcome to my factory!", "Here is your token:\n http://localhost:8080/check-token-renew-password?token=" + token);
                return "register-confirm";
            }
            else {
                return "redirect:/register-confirm-status?status=token-existing";
            }
        } else {
            return "redirect:/register-confirm-status?status=renew-password-denied";
        }
    }

    /* confirmTokenRenewPassword method checks valid token and provides user-update-password.html.
    * If token is expired, the method provides notification and client can get new token.
    * Else, in attacked situation, the method only show status warning. */
    @GetMapping("check-token-renew-password")
    public String confirmTokenRenewPassword(@RequestParam(name = "token") String token, Model model) {

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

    /* confirmRegistration method checks expired token and provides new token to customer's email.
    * If token is not expired, showing denied notification. */
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

    /* updateNewPassword method receives password and userId params to update password.
    * New password will be encoded before updating.
    * Completed notification will be sent to html to show to the customer. */
    @PostMapping("update-new-password")
    public String updateNewPassword(@RequestParam(name = "password") String password,
                                    @RequestParam(name = "userId") int userId) {

        Customer customer = customerService.getCustomerByCustomerId(userId);
        customer.setPassword(bCryptPasswordEncoder.encode(password));
        customerService.save(customer);

        return "redirect:/register-confirm-status?status=renew-password-agreed";
    }
}

package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Token;
import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class ConfirmationController {

    private final TokenService tokenService;

    private final CustomerService customerService;

    @Autowired
    public ConfirmationController(TokenService tokenService, CustomerService customerService) {
        this.tokenService = tokenService;
        this.customerService = customerService;
    }


    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam("token") String token) {

        Token customerToken = tokenService.findByToken(token);

        if (customerToken != null && customerToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            customerService.confirmCustomer(customerToken.getCustomer().getCustomerId());
            tokenService.updateConfirm(customerToken.getToken());
            return "redirect:/confirmation-status?status=agreed";
        } else if (customerToken != null && !customerToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            return "redirect:/confirmation-status?status=expired&token=" + token;
        } else {
            return "redirect:/confirmation-status?status=denied";
        }
    }

    @GetMapping("/confirmation-status")
    public String confirmationStatus(@RequestParam (name = "status", required = false) String status,
                                     @RequestParam (name = "token", required = false) String token, Model model) {
        model.addAttribute("status", status);
        model.addAttribute("token", token);
        return "confirmation-status";
    }
}

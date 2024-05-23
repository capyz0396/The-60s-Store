package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Service.CartService;
import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.EmailSenderService;
import org.example.the60sstore.Service.LanguageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/* Controller of page contact and contain all features of it. */
@Controller
public class ContactController {

    private final CartService cartService;
    private final CustomerService customerService;
    private final LanguageService languageService;
    private final EmailSenderService emailSenderService;

    /* To resolve, the controller needs to create 3 services. */
    public ContactController(CartService cartService, CustomerService customerService, LanguageService languageService, EmailSenderService emailSenderService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.languageService = languageService;
        this.emailSenderService = emailSenderService;
    }

    /* Set url "/contact" for "store-contact.html". */
    @GetMapping({"/contact"})
    public String toContact(HttpSession session, HttpServletRequest request, Model model) {
        cartService.addNumCart(session, model);
        customerService.addLogged(session, model);
        languageService.addLanguagle(request, model);
        return "store-contact";
    }

    /* When a customer fill all inputs and submit, data will be sent to this method.
    * emailSenderService uses those data and send email to shop's owner.
    * Add sent attribute to show notification success after sending. */
    @PostMapping({"/sent-message"})
    public String sentMessage(@RequestParam String name, @RequestParam String email,
                              @RequestParam String subject, @RequestParam String message,
                              HttpSession session, Model model) {

        emailSenderService.sendEmail("longlhfx02906@funix.edu.vn", "CUSTOMER MAIL: " + email,
                "SUBJECT: " + subject + "\nCUSTOMER NAME: " + name +
                        "\nCONTENT: " + message);
        customerService.addLogged(session, model);
        model.addAttribute("sent", "success");

        return "store-home";
    }
}

package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Service.CustomerService;
import org.example.the60sstore.Service.LanguageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/* Controller of About page */
@Controller
public class AboutController {

    private final CustomerService customerService;
    private final LanguageService languageService;

    /* This controller needs create 2 service objects to show login status and language. */
    public AboutController(CustomerService customerService, LanguageService languageService) {
        this.customerService = customerService;
        this.languageService = languageService;
    }

    /* Move customer to about.html view.
    * toAbout add login status and current language getting from session before sending. */
    @GetMapping({"/about"})
    public String toAbout(HttpSession session, HttpServletRequest request, Model model) {
        customerService.addLogged(session, model);
        languageService.addLanguagle(request, model);
        return "store-about";
    }
}

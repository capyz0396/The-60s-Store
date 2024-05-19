package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/* LoginController resolves feature related login page. */
@Controller
public class LoginController {

    /* If a customer is not logged, they can access to login page.
    * Else redirecting to home page. */
    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals("anonymousUser")) {
            session.setAttribute("logged", true);
            return "redirect:home";
        }
        session.setAttribute("logged", false);
        return "user-login";
    }
}

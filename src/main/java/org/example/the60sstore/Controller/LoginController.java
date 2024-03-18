package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

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

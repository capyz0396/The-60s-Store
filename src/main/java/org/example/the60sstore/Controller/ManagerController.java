package org.example.the60sstore.Controller;


import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerController {

    private final CartService cartService;

    @Autowired
    public ManagerController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/profile")
    public String userManager(HttpSession session, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        cartService.addNumCart(session, model);

        if ("ROLE_ADMIN".equals(role)) {
            return "admin-profile";
        } else if ("ROLE_USER".equals(role)) {
            return "user-profile";
        } else {
            return "owner-profile";
        }
    }
}

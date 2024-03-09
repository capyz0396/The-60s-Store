package org.example.the60sstore.Controller;


import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ManagerController {

    private final CartService cartService;

    @Autowired
    public ManagerController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/manager")
    public String manager(HttpSession session, Model model) {
        cartService.addNumCart(session, model);
        return "user-manager";
    }
}

package org.example.the60sstore.Service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class CartService {

    public void addNumCart(HttpSession session, Model model) {
        if (session.getAttribute("cartSize") != null) {
            model.addAttribute("cartSize", session.getAttribute("cartSize"));
        }
    }

    public void resetNumCart(HttpSession session, Model model) {
        if (session.getAttribute("cartSize") != null) {
            session.setAttribute("cart", null);
            session.setAttribute("cartSize", null);
            model.addAttribute("cartSize", session.getAttribute("cartSize"));
        }
    }
}

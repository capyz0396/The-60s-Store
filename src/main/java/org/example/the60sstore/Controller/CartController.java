package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    private final ProductService productService;

    public CartController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/cart")
    public String cart() {
        return "store-cart";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam int productId, HttpSession session, RedirectAttributes redirectAttributes) {

        List<Product> cart = (List<Product>) session.getAttribute("cart");
        int cartSize = 0;

        if (cart == null) {
            cart = new ArrayList<>();
            Product selectedProduct = productService.getProductByProductId(productId);
            selectedProduct.setQuantity(1);
            cart.add(selectedProduct);
            session.setAttribute("cart", cart);
            cartSize++;
        } else {
            boolean alreadyProduct = false;
            for (Product product: cart) {
                cartSize += product.getQuantity();
                if (product.getProductId() == productId) {
                    product.setQuantity(product.getQuantity() + 1);
                    cartSize++;
                    alreadyProduct = true;
                    session.setAttribute("cart", cart);
                }
            }

            if (!alreadyProduct) {
                Product selectedProduct = productService.getProductByProductId(productId);
                selectedProduct.setQuantity(1);
                cartSize++;
                cart.add(selectedProduct);
                session.setAttribute("cart", cart);
            }
        }

        redirectAttributes.addAttribute("cartSize", cartSize);
        return "redirect:/store-product";
    }
}

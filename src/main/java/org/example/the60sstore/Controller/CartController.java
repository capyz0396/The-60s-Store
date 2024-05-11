package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Service.LanguageService;
import org.example.the60sstore.Service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/* This controller controls and executes features related customer's cart. */
@Controller
public class CartController {

    private final LanguageService languageService;
    private final ProductService productService;

    /* Controller needs to create languageService and productService to use. */
    public CartController(LanguageService languageService, ProductService productService) {
        this.languageService = languageService;
        this.productService = productService;
    }

    /* toCart method redirect customer to store-cart.html.
    * Before redirecting, this method add quantity of products if a customer added.
    * More, the method adds "lang" attribute to html show exactly language customer chosen.  */
    @GetMapping("/cart")
    public String toCart(HttpServletRequest request,
                       HttpSession session,
                       Model model) {

        List<Product> products = (List<Product>) session.getAttribute("cart");
        int total = 0;

        if (products != null) {
            for (Product product: products) {
               total += product.getQuantity() * product.getProductPrices().getLast().getPrice();
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("total", total);
        languageService.addLanguagle(request, model);

        return "store-cart";
    }

    /* addToCard method checks session and add more 1 value to product customer chosen. */
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam int productId, HttpSession session) {

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

        session.setAttribute("cartSize", cartSize);

        return "redirect:/product";
    }

    /* removeOutCart method checks product in session and remove it.
    * After that, updating quantity product cart and transfer it to html. */
    @GetMapping("/removeOutCart")
    public String removeOutCart(HttpServletRequest request,
                       HttpSession session,
                       Model model,
                       @RequestParam int productId) {

        List<Product> products = (List<Product>) session.getAttribute("cart");

        int index = -1;
        int quantity = 0;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId().equals(productId)) {
                index = i;
                quantity = products.get(i).getQuantity();
                break;
            }
        }

        if (index != -1) {
            products.remove(index);
        }

        int cartSize = (int) session.getAttribute("cartSize");
        cartSize = cartSize - quantity;

        session.setAttribute("cart", products);
        model.addAttribute("products", products);
        languageService.addLanguagle(request, model);
        session.setAttribute("cartSize", cartSize);

        return "forward:cart";
    }
}

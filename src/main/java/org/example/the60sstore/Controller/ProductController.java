package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Entity.ProductPrice;
import org.example.the60sstore.Service.ProductPriceService;
import org.example.the60sstore.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final ProductPriceService productPriceService;

    @Autowired
    public ProductController(ProductService productService, ProductPriceService productPriceService) {
        this.productService = productService;
        this.productPriceService = productPriceService;
    }

    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "store-add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(
            @RequestParam String productNameEn,
            @RequestParam String productNameVi,
            @RequestParam String origin,
            @RequestParam String imgUrl,
            @RequestParam String descriptionEn,
            @RequestParam String descriptionVi,
            @RequestParam int price) {

        Product product = new Product();
        product.setProductNameEn(productNameEn);
        product.setProductNameVi(productNameVi);
        product.setOrigin(origin);
        product.setImgUrl(imgUrl);
        product.setDescriptionEn(descriptionEn);
        product.setDescriptionVi(descriptionVi);
        productService.addProduct(product);

        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct(productService.getProductByNameEn(productNameEn));
        productPrice.setPrice(price);
        productPrice.setStartDate(LocalDateTime.now());
        productPriceService.addProductPrice(productPrice);

        return "store-home";
    }

    @GetMapping("/edit-price")
    public String showEditPriceForm(Model model) {

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("productPrice", new ProductPrice());

        return "store-edit-price";
    }

    @PostMapping("edited-price")
    public String editPrice(@RequestParam("productId") int productId,
                            @RequestParam("price") int price) {
        productPriceService.updateEndDateByProductId(productId);
        ProductPrice oldProductPrice = productPriceService.getProductPriceByProductId(productId).getFirst();
        ProductPrice newProductPrice = new ProductPrice();
        newProductPrice.setProduct(oldProductPrice.getProduct());
        newProductPrice.setPrice(price);
        newProductPrice.setStartDate(LocalDateTime.now());
        productPriceService.addProductPrice(newProductPrice);
        return "store-home";
    }

    @GetMapping("/product")
    public String showProductList(Model model, @ModelAttribute("cartSize") String cartSize) {

        List<Product> products = productService.getAllProducts();

        for (Product product : products) {
            List<ProductPrice> prices = productPriceService.getProductPriceByProduct(product);
            product.setProductPrices(prices);
        }

        model.addAttribute("products", products);
        model.addAttribute("cartSize", cartSize);

        return "store-product";
    }
}
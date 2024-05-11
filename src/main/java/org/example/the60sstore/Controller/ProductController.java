package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Entity.ProductPrice;
import org.example.the60sstore.Service.CartService;
import org.example.the60sstore.Service.LanguageService;
import org.example.the60sstore.Service.ProductPriceService;
import org.example.the60sstore.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

/* ProductController resolves features related products. */
@Controller
public class ProductController {

    private final CartService cartService;
    private final LanguageService languageService;
    private final ProductService productService;
    private final ProductPriceService productPriceService;

    /* The controller needs to create 4 services below. */
    @Autowired
    public ProductController(CartService cartService, LanguageService languageService, ProductService productService, ProductPriceService productPriceService) {
        this.cartService = cartService;
        this.languageService = languageService;
        this.productService = productService;
        this.productPriceService = productPriceService;
    }

    /* Add url add-product to show store-add-product.html.
    * If cart contains product in there, it will be showed. */
    @GetMapping("/add-product")
    public String showAddProductForm(HttpSession session, Model model) {
        model.addAttribute("product", new Product());
        cartService.addNumCart(session, model);
        return "store-add-product";
    }

    /* saveProduct method receives many param from store-add-product.html.
    * Using them to create new product and product price.
    * After that, redirect to /home. */
    @PostMapping("/save-product")
    public String saveProduct(@RequestParam String productNameEn, @RequestParam String productNameVi,
            @RequestParam String originEn, @RequestParam String originVi,
            @RequestParam String productTypeEn, @RequestParam String productTypeVi,
            @RequestParam String imgUrl, @RequestParam String descriptionEn,
            @RequestParam String descriptionVi, @RequestParam int price) {

        Product product = new Product();
        product.setProductNameEn(productNameEn);
        product.setProductNameVi(productNameVi);
        product.setOriginEn(originEn);
        product.setOriginVi(originVi);
        product.setProductTypeEn(productTypeEn);
        product.setProductTypeVi(productTypeVi);
        product.setImgUrl(imgUrl);
        product.setDescriptionEn(descriptionEn);
        product.setDescriptionVi(descriptionVi);
        productService.addProduct(product);

        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct(productService.getProductByNameEn(productNameEn));
        productPrice.setPrice(price);
        productPrice.setStartDate(LocalDateTime.now());
        productPriceService.addProductPrice(productPrice);

        return "redirect:home";
    }

    /* Add url edit-price to show store-edit-price.html.
    * cartService add quantity to show at cart logo. */
    @GetMapping("/edit-price")
    public String showEditPriceForm(HttpSession session, Model model) {

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("productPrice", new ProductPrice());
        cartService.addNumCart(session, model);

        return "store-edit-price";
    }

    /* editPrice method receive productId and new price to update them at database.
    * When completing, redirect to /home. */
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
        return "redirect:home";
    }

    /* toProduct set url /product to show store-product.html.
    * productService get all product from database and use productPriceService add price to them.
    * After that, add quantity in cart and language to show correctly. */
    @GetMapping("/product")
    public String toProduct(HttpServletRequest request,
                                  HttpSession session,
                                  Model model) {

        List<Product> products = productService.getAllProducts();

        for (Product product : products) {
            List<ProductPrice> prices = productPriceService.getProductPriceByProduct(product);
            product.setProductPrices(prices);
        }

        model.addAttribute("products", products);
        cartService.addNumCart(session, model);
        languageService.addLanguagle(request, model);
        return "store-product";
    }

    /* Depending on type of product, productService get product list by type param.
    * productPriceService add price to them.
    * Finally, add quantity in cart and current language to show at html. */
    @GetMapping("/productType")
    public String showProductListByType(HttpServletRequest request,
                                  HttpSession session,
                                  Model model,
                                         @RequestParam("typeEn") String typeEn) {

        List<Product> productListByType = productService.getAllProductByType(typeEn);

        for (Product product : productListByType) {
            List<ProductPrice> prices = productPriceService.getProductPriceByProduct(product);
            product.setProductPrices(prices);
        }

        model.addAttribute("products", productListByType);
        cartService.addNumCart(session, model);
        languageService.addLanguagle(request, model);
        return "store-product";
    }

    /* showSortedProductList contains 4 types of sorting.
    * If selected param is equal type of sorting, productService get sorted product list like that.
    * Before finishing, add quantity in cart and language to show at html. */
    @PostMapping("/sort-product")
    public String showSortedProductList(HttpServletRequest request,
                                        HttpSession session,
                                        Model model,
                                        @RequestParam("selected") String selected) {

        List<Product> products = switch (selected) {
            case "sorta" -> productService.getAllProductSortedByNameEnAsc();
            case "sortz" -> productService.getAllProductSortedByNameEnDesc();
            case "sortl" -> productService.getAllProductSortedByPriceAsc();
            case "sorth" -> productService.getAllProductSortedByPriceDesc();
            default -> null;
        };

        model.addAttribute("products", products);
        cartService.addNumCart(session, model);
        languageService.addLanguagle(request, model);
        return "store-product";
    }

    /* showSearchedProductList uses keyword param to get product.
    * Search process using productService to get product list.
    * Add quantity in cart and language to show at html. */
    @PostMapping("/search-product")
    public String showSearchedProductList(HttpServletRequest request,
                                          HttpSession session,
                                          Model model,
                                          @RequestParam("keyword") String keyword) {
        System.out.println(keyword);
        List<Product> products = productService.getAllProductContainKeyword(keyword);
        model.addAttribute("products", products);
        cartService.addNumCart(session, model);
        languageService.addLanguagle(request, model);
        return "store-product";
    }

    /* toDetailProduct receives productId in param and use it to get product.
    * It get product by productService.
    * Before completing, the method add quantity cart and language to model, return html. */
    @PostMapping("/detailProduct")
    public String toDetailProduct(HttpServletRequest request,
                                    HttpSession session,
                                    Model model,
                                    @RequestParam int productId) {

        Product selectedProduct = productService.getProductByProductId(productId);
        model.addAttribute("product", selectedProduct);
        cartService.addNumCart(session, model);
        languageService.addLanguagle(request, model);
        return "store-detail";
    }
}

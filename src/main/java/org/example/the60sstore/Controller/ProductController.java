package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.InvoiceDetail;
import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Entity.ProductPrice;
import org.example.the60sstore.Service.CartService;
import org.example.the60sstore.Service.LanguageService;
import org.example.the60sstore.Service.ProductPriceService;
import org.example.the60sstore.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/* ProductController resolves features related products. */
@Controller
public class ProductController {

    private final CartService cartService;
    private final LanguageService languageService;
    private final ProductService productService;
    private final ProductPriceService productPriceService;

    private final LocaleResolver localeResolver;

    /* The controller needs to create 4 services below. */
    @Autowired
    public ProductController(CartService cartService, LanguageService languageService, ProductService productService, ProductPriceService productPriceService, LocaleResolver localeResolver) {
        this.cartService = cartService;
        this.languageService = languageService;
        this.productService = productService;
        this.productPriceService = productPriceService;
        this.localeResolver = localeResolver;
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


    /* Add url edit-price to show store-price.html.
     * cartService add quantity to show at cart logo. */
    @GetMapping("/store-price")
    public String showProductPrice(HttpSession session, HttpServletRequest request,
                                   Model model, @ModelAttribute("editPrice") String editPrice) {

        List<Product> products = productService.getAllProducts();

        for (Product product : products) {
            List<ProductPrice> prices = productPriceService.getProductPriceByProduct(product);
            product.setProductPrices(prices);
        }

        model.addAttribute("products", products);
        model.addAttribute("editPrice", editPrice);
        languageService.addLanguagle(request, model);
        cartService.addNumCart(session, model);

        return "store-price";
    }

    @GetMapping("/store-price/{id}")
    public String showEditPrice(HttpServletRequest request,
                                     @PathVariable("id") int productId,
                                     Model model) {
        Product product = productService.getProductByProductId(productId);
        model.addAttribute("product", product);
        languageService.addLanguagle(request, model);
        return "store-price-edit";
    }

    @GetMapping("/store-price-history/{id}")
    public String showPriceHistory(HttpServletRequest request,
                                @PathVariable("id") int productId,
                                Model model) {
        Product product = productService.getProductByProductId(productId);
        List <ProductPrice> productPriceList = productPriceService.getProductPriceByProduct(product);
        model.addAttribute("product", product);
        model.addAttribute("productPriceList", productPriceList);
        languageService.addLanguagle(request, model);
        return "store-price-history";
    }

    /* editPrice method receive productId and new price to update them at database.
    * When completing, redirect to /home. */
    @PostMapping("edited-price")
    public String editPrice(RedirectAttributes redirectAttributes, @RequestParam("productId") int productId,
                            @RequestParam("price") int price) {
        productPriceService.updateEndDateByProductId(productId);
        ProductPrice oldProductPrice = productPriceService.getProductPriceByProductId(productId).getFirst();
        ProductPrice newProductPrice = new ProductPrice();
        newProductPrice.setProduct(oldProductPrice.getProduct());
        newProductPrice.setPrice(price);
        newProductPrice.setStartDate(LocalDateTime.now());
        productPriceService.addProductPrice(newProductPrice);
        redirectAttributes.addFlashAttribute("editPrice", "success");
        return "redirect:store-price";
    }

    /* toProduct set url /product to show store-product.html.
    * productService get all product from database and use productPriceService add price to them.
    * After that, add quantity in cart and language to show correctly. */
    @GetMapping("/product")
    public String toProduct(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
                            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                            @RequestParam(value = "sort", required = false, defaultValue = "default") String sortType,
                            HttpServletRequest request, HttpSession session, Model model) {

        PageRequest pageRequest = PageRequest.of(page,9);
        Page<Product> productPage = null;

        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();

        /* Having filter and not having sort */
        if (!filter.isEmpty() && sortType.equals("default")) {
            productPage = productService.getAllProductByFilter(filter, pageRequest);
            model.addAttribute("filter", filter);
        }
        /* Having filter and having sort */
        else if (!filter.isEmpty()) {
            productPage = productService.getAllProductByFilterAndSort(filter, pageRequest, sortType, language);
            model.addAttribute("filter", filter);
        }

        /* Having keyword and not having sort */
        else if (!keyword.isEmpty() && sortType.equals("default")) {
            productPage = productService.getAllProductByKeyword(keyword, pageRequest, language);
            model.addAttribute("keyword", keyword);
        }
        /* Having keyword and having sort */
        else if (!keyword.isEmpty()){
            productPage = productService.getAllProductByKeywordAndSort(keyword, pageRequest, sortType, language);
            model.addAttribute("keyword", keyword);
        }
        /* Only having sort */
        else if (filter.isEmpty() && keyword.isEmpty() && !sortType.equals("default")) {
            productPage = productService.getAllProductByPageAndSort(pageRequest, sortType, language);
            model.addAttribute("sort", sortType);
        } else {
            /* Get all product and add productPrice to them. */
            productPage = productService.getAllProductByPage(pageRequest);
        }

        for (Product product : productPage) {
            List<ProductPrice> prices = productPriceService.getProductPriceByProduct(product);
            product.setProductPrices(prices);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
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

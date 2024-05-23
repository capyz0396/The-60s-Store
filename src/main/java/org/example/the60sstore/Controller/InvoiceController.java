package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.*;
import org.example.the60sstore.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/* InvoiceController solves features related invoice. */
@Controller
public class InvoiceController {

    private final CartService cartService;
    private final CustomerService customerService;
    private final CustomerLevelService customerLevelService;
    private final InvoiceService invoiceService;
    private final InvoiceDetailService invoiceDetailService;
    private final LanguageService languageService;
    private final ProductService productService;

    /* To support features, the controller needs to create below services. */
    @Autowired
    public InvoiceController(CartService cartService,
                             CustomerService customerService,
                             CustomerLevelService customerLevelService,
                             InvoiceService invoiceService,
                             InvoiceDetailService invoiceDetailService,
                             LanguageService languageService,
                             ProductService productService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.customerLevelService = customerLevelService;
        this.invoiceService = invoiceService;
        this.invoiceDetailService = invoiceDetailService;
        this.languageService = languageService;
        this.productService = productService;
    }

    /* createInvoice method will get productList and prices from param.
     * Calculating total amount and create invoice by invoiceService at database.
     * After that, invoiceDetailService creates one by one product to database. */
    @PostMapping("/create-invoice")
    public String createInvoice(HttpSession session,
                                Model model,
                                @RequestParam(required = false, defaultValue = "") List<String> productNameEn,
                                @RequestParam(required = false, defaultValue = "") List<Integer> quantity,
                                @RequestParam(required = false, defaultValue = "") List<Integer> price,
                                @RequestParam(required = false, defaultValue = "") String shippingAddress) {
        if (!productNameEn.isEmpty()) {
            if (productNameEn.size() == quantity.size() && quantity.size() == price.size()) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Customer customer = null;
                if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                    Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
                    customer = customerService.getCustomerByEmail(attributes.get("email").toString());
                }
                if (authentication.getPrincipal() instanceof Customer) {
                    customer = (Customer) authentication.getPrincipal();
                }

                Invoice invoice = new Invoice();
                invoice.setInvoiceDate(LocalDateTime.now());
                if (!shippingAddress.isEmpty()) {
                    invoice.setShippingAddress(shippingAddress);
                } else {
                    assert customer != null;
                    invoice.setShippingAddress(customer.getAddress());
                }
                invoice.setTotalAmount(BigDecimal.ZERO);
                invoice.setCustomer(customer);
                invoice.setInvoiceStatus("Waiting");

                invoice = invoiceService.save(invoice);
                BigDecimal totalAmount = BigDecimal.ZERO;

                for (int i = 0; i < productNameEn.size(); i++) {

                    Product product = productService.getProductByNameEn(productNameEn.get(i));
                    InvoiceDetail invoiceDetail = new InvoiceDetail();
                    invoiceDetail.setInvoice(invoice);
                    invoiceDetail.setProduct(product);
                    invoiceDetail.setQuantity(quantity.get(i));
                    invoiceDetail.setSubtotal(BigDecimal.valueOf((long) price.get(i) * quantity.get(i)));
                    totalAmount = totalAmount.add(invoiceDetail.getSubtotal());
                    invoiceDetailService.save(invoiceDetail);
                }

                invoice.setTotalAmount(totalAmount);
                invoiceService.save(invoice);
                cartService.resetNumCart(session, model);
                customerService.addLogged(session, model);
                model.addAttribute("order", "success");
            }
            return "store-home";
        }
        return "redirect:/cart";
    }

    /* When an invoice is confirmed successfully, updateCustomerLevel update level of the customer.
     * By Loyalty Point, the method can update exactly level. */
    private void updateCustomerLevel(Customer customer) {

        List<CustomerLevel> customerLevels = customerLevelService.getAll();

        for (CustomerLevel level : customerLevels) {
            if (customer.getLoyaltyPoint() >= level.getMinPoints() &&
                    (level.getMaxPoints() == null || customer.getLoyaltyPoint() <= level.getMaxPoints())) {
                customer.setCustomerLevel(level);
                break;
            }
        }
    }

    /* showInvoices method provide invoices by invoiceService.
     * This adds language to show invoice list suitable at manager-invoice.html. */
    @GetMapping("/invoice")
    public String showInvoices(HttpServletRequest request, Model model) {
        List<Invoice> invoices = invoiceService.getAll();
        model.addAttribute("invoices", invoices);
        languageService.addLanguagle(request, model);
        return "manager-invoice";
    }

    /* showInvoiceDetails method gets id by url and find invoice detail list by it.
     * After that, list will be added to model and show at manager-detail.html. */
    @GetMapping("/invoice/{id}")
    public String showInvoiceDetails(HttpServletRequest request,
                                     @PathVariable("id") int invoiceId,
                                     Model model) {
        List<InvoiceDetail> invoiceDetails = invoiceDetailService.findByInvoiceId(invoiceId);
        model.addAttribute("invoiceDetails", invoiceDetails);
        languageService.addLanguagle(request, model);
        return "manager-detail";
    }

    /* actionInvoice method get id by url and action by param.
     * The method checks invoice by id and update status of it.
     * If action equal complete, update loyalty point of the customer and return last page.
     * Else only updating invoice status and return last page. */
    @GetMapping("/invoice/{id}/action")
    public String actionInvoice(HttpServletRequest request,
                                @PathVariable("id") int invoiceId,
                                @RequestParam("action") String action) {

        Invoice invoice = invoiceService.getInvoiceByInvoiceId(invoiceId);

        if (action.equals("Complete")) {
            Customer customer = invoice.getCustomer();
            BigDecimal loyaltyPointsEarned = invoice.getTotalAmount().divide(BigDecimal.valueOf(10000), 0, RoundingMode.DOWN);
            customer.setLoyaltyPoint(customer.getLoyaltyPoint() + loyaltyPointsEarned.intValue());
            updateCustomerLevel(customer);
            customerService.save(customer);
        }
        invoice.setInvoiceStatus(action);
        invoiceService.save(invoice);

        String referrer = request.getHeader("referer");
        if (referrer != null && referrer.contains(request.getContextPath())) {
            return "redirect:" + referrer;
        } else {
            return "redirect:/";
        }
    }

    /* toUserInvoice method gets all customer's invoices and show at user-invoice.html.
     * The method gets customer information from SecurityContextHolder. */
    @GetMapping("/user-invoice")
    public String toUserInvoice(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = null;

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
            customer = customerService.getCustomerByEmail(attributes.get("email").toString());
        }
        if (authentication.getPrincipal() instanceof Customer) {
            customer = (Customer) authentication.getPrincipal();
        }

        assert customer != null;
        List<Invoice> invoiceList = invoiceService.getInvoiceByCustomerUserOrderByDateDesc(customer.getUsername());
        model.addAttribute("invoiceList", invoiceList);
        return "user-invoice";
    }

    /* showUserInvoiceDetails method gets id by url when the customer access.
     * Using this url to get invoice detail and add these to show at manager-detail.html.
     * If customers access url of invoice id not of them, redirect to user-invoice.html. */
    @GetMapping("/user-invoice/{id}")
    public String showUserInvoiceDetails(HttpServletRequest request,
                                         @PathVariable("id") int invoiceId,
                                         Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = null;

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
            customer = customerService.getCustomerByEmail(attributes.get("email").toString());
        }
        if (authentication.getPrincipal() instanceof Customer) {
            customer = (Customer) authentication.getPrincipal();
        }

        Invoice invoice = invoiceService.getInvoiceByInvoiceId(invoiceId);
        assert customer != null;
        if (invoice.getCustomer().getUsername().equals(customer.getUsername())) {
            List<InvoiceDetail> invoiceDetails = invoiceDetailService.findByInvoiceId(invoiceId);
            model.addAttribute("invoiceDetails", invoiceDetails);
            languageService.addLanguagle(request, model);
            return "manager-detail";
        } else {
            return "redirect:/user-invoice";
        }
    }
}

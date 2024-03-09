package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.Invoice;
import org.example.the60sstore.Entity.InvoiceDetail;
import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class InvoiceController {

    private final CartService cartService;
    private final CustomerService customerService;
    private final InvoiceService invoiceService;
    private final InvoiceDetailService invoiceDetailService;
    private final LanguageService languageService;
    private final ProductService productService;

    @Autowired
    public InvoiceController(CartService cartService, CustomerService customerService,
                             InvoiceService invoiceService,
                             InvoiceDetailService invoiceDetailService,
                             LanguageService languageService,
                             ProductService productService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.invoiceService = invoiceService;
        this.invoiceDetailService = invoiceDetailService;
        this.languageService = languageService;
        this.productService = productService;
    }

    @PostMapping("/create-invoice")
    public String createInvoice(HttpSession session,
                                Model model,
                                @RequestParam List<String> productNameEn,
                                @RequestParam List<Integer> quantity,
                                @RequestParam List<Integer> price,
                                @RequestParam String shippingAdress) {

        if (productNameEn.size() == quantity.size() && quantity.size() == price.size()) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Customer customer = (Customer) authentication.getPrincipal();

            Invoice invoice = new Invoice();
            invoice.setInvoiceDate(LocalDateTime.now());
            if (!shippingAdress.isEmpty()) {
                invoice.setShippingAddress(shippingAdress);
            }
            else {
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

        }

        return "store-home";
    }

    @GetMapping("/invoice")
    public String showInvoices(HttpServletRequest request, Model model) {
        List<Invoice> invoices = invoiceService.getAll();
        model.addAttribute("invoices", invoices);
        languageService.addLanguagle(request, model);
        return "manager-invoice";
    }

    @GetMapping("/invoice/{id}")
    public String showInvoiceDetails(HttpServletRequest request,
                                     @PathVariable("id") int invoiceId,
                                     Model model) {
        List<InvoiceDetail> invoiceDetails = invoiceDetailService.findByInvoiceId(invoiceId);
        model.addAttribute("invoiceDetails", invoiceDetails);
        languageService.addLanguagle(request, model);
        return "manager-detail";
    }

    @GetMapping("/invoice/{id}/action")
    public String completeInvoice(HttpServletRequest request,
                                  @PathVariable("id") int invoiceId,
                                  @RequestParam("action") String action) {

        Invoice invoice = invoiceService.getInvoiceByInvoiceId(invoiceId);
        invoice.setInvoiceStatus(action);
        invoiceService.save(invoice);

        String referrer = request.getHeader("referer");
        if (referrer != null && referrer.contains(request.getContextPath())) {
            return "redirect:" + referrer;
        } else {
            return "redirect:/";
        }
    }
}

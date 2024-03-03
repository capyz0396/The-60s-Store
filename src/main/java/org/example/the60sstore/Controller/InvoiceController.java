package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.Customer;
import org.example.the60sstore.Entity.Invoice;
import org.example.the60sstore.Entity.InvoiceDetail;
import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Service.InvoiceDetailService;
import org.example.the60sstore.Service.InvoiceService;
import org.example.the60sstore.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class InvoiceController {

    private final ProductService productService;
    private final InvoiceService invoiceService;
    private final InvoiceDetailService invoiceDetailService;

    @Autowired
    public InvoiceController(ProductService productService,
                             InvoiceService invoiceService,
                             InvoiceDetailService invoiceDetailService) {
        this.productService = productService;
        this.invoiceService = invoiceService;
        this.invoiceDetailService = invoiceDetailService;
    }

    @PostMapping("/create-invoice")
    public String createInvoice(@RequestParam List<String> productNameEn,
                                @RequestParam List<Integer> quantity,
                                @RequestParam List<Integer> price) {

        if (productNameEn.size() == quantity.size() && quantity.size() == price.size()) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Customer customer = (Customer) authentication.getPrincipal();

            Invoice invoice = new Invoice();
            invoice.setInvoiceDate(LocalDateTime.now());
            invoice.setTotalAmount(BigDecimal.ZERO);
            invoice.setCustomer(customer);

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
        }

        return "store-home";
    }
}

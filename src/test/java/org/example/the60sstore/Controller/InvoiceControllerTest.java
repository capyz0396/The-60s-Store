package org.example.the60sstore.Controller;

import org.example.the60sstore.Entity.*;
import org.example.the60sstore.Service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerLevelService customerLevelService;

    @MockBean
    private InvoiceService invoiceService;

    @MockBean
    private InvoiceDetailService invoiceDetailService;

    @MockBean
    private LanguageService languageService;

    @MockBean
    private ProductService productService;

    @MockBean
    private RoleService roleService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowInvoices() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setEmail("test@example.com");
        customer.setFirstName("Test");
        customer.setLastName("Test");
        customer.setUsername("jdsakdjas");
        customer.setPassword("password");
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(1);
        invoice.setCustomer(customer);
        List<Invoice> invoices = Arrays.asList(invoice);
        when(invoiceService.getAll()).thenReturn(invoices);

        mockMvc.perform(MockMvcRequestBuilders.get("/invoice"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-invoice"))
                .andExpect(model().attributeExists("invoices"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowInvoiceDetails() throws Exception {

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(1);
        invoice.setCustomer(new Customer());

        ProductPrice productPrice = new ProductPrice();
        productPrice.setPrice(11000);
        List<ProductPrice> productPrices = Arrays.asList(productPrice);

        Product product = new Product();
        product.setProductId(1);
        product.setProductNameEn("Product 1");
        product.setProductPrices(productPrices);

        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setInvoice(invoice);
        invoiceDetail.setProduct(product);

        List<InvoiceDetail> invoiceDetails = Arrays.asList(invoiceDetail);
        when(invoiceDetailService.findByInvoiceId(1)).thenReturn(invoiceDetails);

        mockMvc.perform(MockMvcRequestBuilders.get("/invoice/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-detail"))
                .andExpect(model().attributeExists("invoiceDetails"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testActionInvoice() throws Exception {

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(1);
        invoice.setCustomer(new Customer());
        invoice.setTotalAmount(BigDecimal.valueOf(10000));

        ProductPrice productPrice = new ProductPrice();
        productPrice.setPrice(11000);
        List<ProductPrice> productPrices = Arrays.asList(productPrice);

        Product product = new Product();
        product.setProductId(1);
        product.setProductNameEn("Product 1");
        product.setProductPrices(productPrices);

        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setInvoice(invoice);
        invoiceDetail.setProduct(product);

        when(invoiceService.getInvoiceByInvoiceId(1)).thenReturn(invoice);

        mockMvc.perform(MockMvcRequestBuilders.get("/invoice/1/action")
                        .param("action", "Complete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(customerService).save(any(Customer.class));
        verify(invoiceService).save(any(Invoice.class));
    }
}

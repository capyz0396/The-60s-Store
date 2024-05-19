package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Entity.ProductPrice;
import org.example.the60sstore.Service.CartService;
import org.example.the60sstore.Service.LanguageService;
import org.example.the60sstore.Service.ProductPriceService;
import org.example.the60sstore.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private LanguageService languageService;

    @Mock
    private ProductService productService;

    @Mock
    private ProductPriceService productPriceService;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testShowAddProductForm() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/add-product").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("store-add-product"))
                .andExpect(model().attributeExists("product"));

        verify(cartService).addNumCart(any(HttpSession.class), any(Model.class));
    }

    @Test
    public void testSaveProduct() throws Exception {
        when(productService.getProductByNameEn(anyString())).thenReturn(new Product());

        mockMvc.perform(post("/save-product")
                        .param("productNameEn", "TestProduct")
                        .param("productNameVi", "TestProductVi")
                        .param("originEn", "USA")
                        .param("originVi", "Mỹ")
                        .param("productTypeEn", "Electronics")
                        .param("productTypeVi", "Điện tử")
                        .param("imgUrl", "test.jpg")
                        .param("descriptionEn", "Description")
                        .param("descriptionVi", "Mô tả")
                        .param("price", "1000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("home"));

        verify(productService).addProduct(any(Product.class));
        verify(productPriceService).addProductPrice(any(ProductPrice.class));
    }

    @Test
    public void testShowEditPriceForm() throws Exception {
        List<Product> products = Collections.singletonList(new Product());
        when(productService.getAllProducts()).thenReturn(products);

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/edit-price").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("store-edit-price"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("productPrice"));

        verify(cartService).addNumCart(any(HttpSession.class), any(Model.class));
    }

    @Test
    public void testEditPrice() throws Exception {
        Product product = new Product();
        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct(product);
        when(productPriceService.getProductPriceByProductId(anyInt())).thenReturn(Collections.singletonList(productPrice));

        mockMvc.perform(post("/edited-price")
                        .param("productId", "1")
                        .param("price", "2000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("home"));

        verify(productPriceService).updateEndDateByProductId(anyInt());
        verify(productPriceService).addProductPrice(any(ProductPrice.class));
    }

    @Test
    public void testToProduct() throws Exception {
        List<Product> products = Collections.singletonList(new Product());
        when(productService.getAllProducts()).thenReturn(products);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/product").requestAttr("request", request).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("store-product"))
                .andExpect(model().attributeExists("products"));

        verify(cartService).addNumCart(any(HttpSession.class), any(Model.class));
        verify(languageService).addLanguagle(any(HttpServletRequest.class), any(Model.class));
    }
}

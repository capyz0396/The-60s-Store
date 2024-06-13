package org.example.the60sstore.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Entity.ProductPrice;
import org.example.the60sstore.Service.CartService;
import org.example.the60sstore.Service.LanguageService;
import org.example.the60sstore.Service.ProductPriceService;
import org.example.the60sstore.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private LanguageService languageService;

    @Mock
    private LocaleResolver localeResolver;

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
    public void testToProductDefault() throws Exception {
        when(localeResolver.resolveLocale(any())).thenReturn(Locale.ENGLISH);
        when(productService.getAllProductByPage(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(view().name("store-product"))
                .andExpect(model().attributeExists("productPage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));

        verify(productService).getAllProductByPage(any());
        verify(cartService).addNumCart(any(), any());
        verify(languageService).addLanguagle(any(), any());
    }

    @Test
    public void testToProductWithFilter() throws Exception {
        when(localeResolver.resolveLocale(any())).thenReturn(Locale.ENGLISH);
        when(productService.getAllProductByFilter(anyString(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/product").param("filter", "someFilter"))
                .andExpect(status().isOk())
                .andExpect(view().name("store-product"))
                .andExpect(model().attributeExists("productPage"))
                .andExpect(model().attribute("filter", "someFilter"));

        verify(productService).getAllProductByFilter(anyString(), any());
    }

    @Test
    public void testToProductWithKeyword() throws Exception {
        when(localeResolver.resolveLocale(any())).thenReturn(Locale.ENGLISH);
        when(productService.getAllProductByKeyword(anyString(), any(), anyString())).thenReturn(Page.empty());

        mockMvc.perform(get("/product").param("keyword", "someKeyword"))
                .andExpect(status().isOk())
                .andExpect(view().name("store-product"))
                .andExpect(model().attributeExists("productPage"))
                .andExpect(model().attribute("keyword", "someKeyword"));

        verify(productService).getAllProductByKeyword(anyString(), any(), anyString());
    }

    @Test
    public void testToProductWithSort() throws Exception {
        when(localeResolver.resolveLocale(any())).thenReturn(Locale.ENGLISH);
        when(productService.getAllProductByPageAndSort(any(), anyString(), anyString())).thenReturn(Page.empty());

        mockMvc.perform(get("/product").param("sort", "price"))
                .andExpect(status().isOk())
                .andExpect(view().name("store-product"))
                .andExpect(model().attributeExists("productPage"))
                .andExpect(model().attribute("sort", "price"));

        verify(productService).getAllProductByPageAndSort(any(), anyString(), anyString());
    }
}

package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProductByProductId(int productId) {
        return productRepository.getProductByProductId(productId);
    }

    public Product getProductByNameEn(String productName) {
        return productRepository.getProductByProductNameEn(productName);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}

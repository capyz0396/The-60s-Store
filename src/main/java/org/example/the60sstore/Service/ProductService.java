package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/* ProductService returns Product or list of them to Controller. */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /* Service always need to create Repository first. */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /* addProduct method uses product object to save or update in database. */
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    /* getProductByProductId method gets product object by productId. */
    public Product getProductByProductId(int productId) {
        return productRepository.getProductByProductId(productId);
    }

    /* getProductByNameEn method gets product by productNameEn. */
    public Product getProductByNameEn(String productName) {
        return productRepository.getProductByProductNameEn(productName);
    }

    /* getAllProducts method gets all products from database. */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /* getAllProductSortedByNameEnAsc method gets all products order them by english name asc. */
    public List<Product> getAllProductSortedByNameEnAsc() {
        return productRepository.findAllByOrderByProductNameEnAsc();
    }

    /* getAllProductSortedByNameEnDesc method gets all products order them by english name desc. */
    public List<Product> getAllProductSortedByNameEnDesc() {
        return productRepository.findAllByOrderByProductNameEnDesc();
    }

    /* getAllProductSortedByPriceAsc method gets all products order by price asc. */
    public List<Product> getAllProductSortedByPriceAsc() {
        return productRepository.findAllByEndDateIsNullOrderByPriceAsc();
    }

    /* getAllProductSortedByPriceDesc method gets all products order by price desc. */
    public List<Product> getAllProductSortedByPriceDesc() {
        return productRepository.findAllByEndDateIsNullOrderByPriceDesc();
    }

    /* getAllProductContainKeyword method gets all products containing keyword param. */
    public List<Product> getAllProductContainKeyword(String keyword) {
        return productRepository.findProductByProductNameEnContaining(keyword);
    }

    /* getAllProductByType method gets all products by type. */
    public List<Product> getAllProductByType(String keyword) {
        return productRepository.findProductsByProductTypeEnContaining(keyword);
    }
}

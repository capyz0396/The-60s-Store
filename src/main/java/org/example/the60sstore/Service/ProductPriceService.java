package org.example.testspring.Service;

import org.example.testspring.Entity.Product;
import org.example.testspring.Entity.ProductPrice;
import org.example.testspring.Repository.ProductPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductPriceService {
    private final ProductPriceRepository productPriceRepository;

    @Autowired
    public ProductPriceService(ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    public void addProductPrice(ProductPrice productPrice) {
        productPriceRepository.save(productPrice);
    }

    public List<ProductPrice> getProductPriceByProduct(Product product) {
        return productPriceRepository.getProductPriceByProductId(product.getProductId());
    }

    public List<ProductPrice> getProductPriceByProductId(int productId) {
        return productPriceRepository.getProductPriceByProductId(productId);
    }

    public void updateEndDateByProductId(int productId) {
        int priceId = getPriceIdByProductId(productId);
        productPriceRepository.updateEndDateIfNull(priceId, LocalDateTime.now());
    }

    public int getPriceIdByProductId(int productId) {
        return productPriceRepository.getProductPriceByProduct_ProductIdOrderByPriceIdDesc(productId).getFirst().getPriceId();
    }
}

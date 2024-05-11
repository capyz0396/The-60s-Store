package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Entity.ProductPrice;
import org.example.the60sstore.Repository.ProductPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/* ProductPriceService returns ProductPrice or list of them to Controller. */
@Service
public class ProductPriceService {

    private final ProductPriceRepository productPriceRepository;

    /* Service always need to create Repository first. */
    @Autowired
    public ProductPriceService(ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    /* addProductPrice method creates new ProductPrice in database or update it. */
    public void addProductPrice(ProductPrice productPrice) {
        productPriceRepository.save(productPrice);
    }

    /* getProductPriceByProduct method uses Product to get ProductPrice. */
    public List<ProductPrice> getProductPriceByProduct(Product product) {
        return productPriceRepository.getProductPriceByProductId(product.getProductId());
    }

    /* getProductPriceByProductId method uses productId to get ProductPrice. */
    public List<ProductPrice> getProductPriceByProductId(int productId) {
        return productPriceRepository.getProductPriceByProductId(productId);
    }

    /* updateEndDateByProductId method gets priceId by productId.
    * After that, updateEndDate of productPrice. */
    public void updateEndDateByProductId(int productId) {
        int priceId = getPriceIdByProductId(productId);
        productPriceRepository.updateEndDateIfNull(priceId, LocalDateTime.now());
    }

    /* getPriceIdByProductId method returns priceId by productId. */
    public int getPriceIdByProductId(int productId) {
        return productPriceRepository.getProductPriceByProduct_ProductIdOrderByPriceIdDesc(productId).getFirst().getPriceId();
    }
}

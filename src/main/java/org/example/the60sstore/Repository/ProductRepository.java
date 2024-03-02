package org.example.the60sstore.Repository;

import org.example.the60sstore.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Transactional
    Product getProductByProductNameEn(String productNameEn);

    @Transactional
    Product getProductByProductId(int productId);
}

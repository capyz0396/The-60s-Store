package org.example.testspring.Repository;

import org.example.testspring.Entity.Customer;
import org.example.testspring.Entity.Product;
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

package org.example.the60sstore.Repository;

import org.example.the60sstore.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Transactional
    Product getProductByProductNameEn(String productNameEn);

    @Transactional
    Product getProductByProductId(int productId);

    List<Product> findAllByOrderByProductNameEnAsc();

    List<Product> findAllByOrderByProductNameEnDesc();

    @Query("SELECT p FROM ProductPrice pp JOIN pp.product p WHERE pp.endDate IS NULL ORDER BY pp.price ASC")
    List<Product> findAllByEndDateIsNullOrderByPriceAsc();

    @Query("SELECT p FROM ProductPrice pp JOIN pp.product p WHERE pp.endDate IS NULL ORDER BY pp.price DESC")
    List<Product> findAllByEndDateIsNullOrderByPriceDesc();

    List<Product> findProductByProductNameEnContaining(String keyword);

    List<Product> findProductsByProductTypeEnContaining(String keyword);
}

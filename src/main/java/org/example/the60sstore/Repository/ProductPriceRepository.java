package org.example.testspring.Repository;

import jakarta.transaction.Transactional;
import org.example.testspring.Entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Integer> {

    @Query("SELECT pp FROM ProductPrice pp " +
            "WHERE pp.product.productId = :productId " +
            "AND :targetDate BETWEEN pp.startDate AND pp.endDate")
    List<ProductPrice> findProductPricesInDateRange(
            @Param("productId") int productId,
            @Param("targetDate") LocalDateTime targetDate
    );

    @Query("SELECT pp FROM ProductPrice pp " +
            "WHERE pp.product.productId = :productId ")
    List<ProductPrice> getProductPriceByProductId(@Param("productId") int productId);

    @Transactional
    @Modifying
    @Query("UPDATE ProductPrice p SET p.endDate = :newEndDate WHERE p.priceId = :priceId AND p.endDate IS NULL")
    void updateEndDateIfNull(@Param("priceId") int priceId, @Param("newEndDate") LocalDateTime newEndDate);

    List<ProductPrice> getProductPriceByProduct_ProductIdOrderByPriceIdDesc(int productId);

}

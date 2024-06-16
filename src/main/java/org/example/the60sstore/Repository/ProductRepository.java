package org.example.the60sstore.Repository;

import org.example.the60sstore.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /* Only Sort All */
    Page<Product> findAllByOrderByProductNameEnAsc(Pageable pageable);

    Page<Product> findAllByOrderByProductNameEnDesc(Pageable pageable);

    Page<Product> findAllByOrderByProductNameViAsc(Pageable pageable);

    Page<Product> findAllByOrderByProductNameViDesc(Pageable pageable);

    /* Filter && Sort */
    Page<Product> findByProductTypeEn(String productTypeEn, Pageable pageable);

    Page<Product> findByProductTypeEnOrderByProductNameEnAsc(String productTypeEn, Pageable pageable);

    Page<Product> findByProductTypeEnOrderByProductNameEnDesc(String productTypeEn, Pageable pageable);

    Page<Product> findByProductTypeEnOrderByProductNameViAsc(String productTypeEn, Pageable pageable);

    Page<Product> findByProductTypeEnOrderByProductNameViDesc(String productTypeEn, Pageable pageable);

    @Query("SELECT p FROM ProductPrice pp JOIN pp.product p WHERE pp.endDate IS NULL AND p.productTypeEn like :productTypeEn ORDER BY pp.price ASC")
    Page<Product> findByProductTypeEnOrderByPriceAsc(@Param("productTypeEn") String productTypeEn, Pageable pageable);

    @Query("SELECT p FROM ProductPrice pp JOIN pp.product p WHERE pp.endDate IS NULL AND p.productTypeEn like :productTypeEn ORDER BY pp.price DESC")
    Page<Product> findByProductTypeEnOrderByPriceDesc(@Param("productTypeEn") String productTypeEn, Pageable pageable);

    @Query("SELECT p FROM ProductPrice pp JOIN pp.product p WHERE pp.endDate IS NULL ORDER BY pp.price ASC")
    Page<Product> findAllOrderByPriceAsc(Pageable pageable);

    @Query("SELECT p FROM ProductPrice pp JOIN pp.product p WHERE pp.endDate IS NULL ORDER BY pp.price DESC")
    Page<Product> findAllOrderByPriceDesc(Pageable pageable);

    /* Search & Sort By 4 Options */
    Page<Product> findProductByProductNameEnContaining(String keyword, Pageable pageable);

    Page<Product> findProductByProductNameViContaining(String keyword, Pageable pageable);

    Page<Product> findProductByProductNameEnContainingOrderByProductNameEnAsc(String keyword, Pageable pageable);

    Page<Product> findProductByProductNameEnContainingOrderByProductNameEnDesc(String keyword, Pageable pageable);

    Page<Product> findProductByProductNameViContainingOrderByProductNameViAsc(String keyword, Pageable pageable);

    Page<Product> findProductByProductNameViContainingOrderByProductNameViDesc(String keyword, Pageable pageable);

    @Query("SELECT p FROM ProductPrice pp JOIN pp.product p WHERE pp.endDate IS NULL AND p.productNameEn like %:productNameEn% ORDER BY pp.price ASC")
    Page<Product> findProductByProductNameEnContainingOrderByPriceAsc(@Param("productNameEn") String productNameEn, Pageable pageable);

    @Query("SELECT p FROM ProductPrice pp JOIN pp.product p WHERE pp.endDate IS NULL AND p.productNameEn like %:productNameEn% ORDER BY pp.price DESC")
    Page<Product> findProductByProductNameEnContainingOrderByPriceDesc(@Param("productNameEn") String productNameEn, Pageable pageable);

    @Query("SELECT p FROM ProductPrice pp JOIN pp.product p WHERE pp.endDate IS NULL AND p.productNameVi like %:productNameVi% ORDER BY pp.price ASC")
    Page<Product> findProductByProductNameViContainingOrderByPriceAsc(@Param("productNameVi") String productNameVi, Pageable pageable);

    @Query("SELECT p FROM ProductPrice pp JOIN pp.product p WHERE pp.endDate IS NULL AND p.productNameVi like %:productNameVi% ORDER BY pp.price DESC")
    Page<Product> findProductByProductNameViContainingOrderByPriceDesc(@Param("productNameVi") String productNameVi, Pageable pageable);
}

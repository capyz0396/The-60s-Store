package org.example.the60sstore.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid")
    private Integer productId;

    @Column(name = "product_name_en")
    private String productNameEn;

    @Column(name = "product_name_vi")
    private String productNameVi;

    @Column(name = "origin_en")
    private String originEn;

    @Column(name = "origin_vi")
    private String originVi;

    @Column(name = "product_type_en")
    private String productTypeEn;

    @Column(name = "product_type_vi")
    private String productTypeVi;

    public String getOriginEn() {
        return originEn;
    }

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "description_vi")
    private String descriptionVi;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductPrice> productPrices;

    private int quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    public String getProductNameVi() {
        return productNameVi;
    }

    public void setProductNameVi(String productNameVi) {
        this.productNameVi = productNameVi;
    }

    public void setOriginEn(String originEn) {
        this.originEn = originEn;
    }

    public String getOriginVi() {
        return originVi;
    }

    public void setOriginVi(String originVi) {
        this.originVi = originVi;
    }

    public String getProductTypeEn() {
        return productTypeEn;
    }

    public void setProductTypeEn(String productTypeEn) {
        this.productTypeEn = productTypeEn;
    }

    public String getProductTypeVi() {
        return productTypeVi;
    }

    public void setProductTypeVi(String productTypeVi) {
        this.productTypeVi = productTypeVi;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionVi() {
        return descriptionVi;
    }

    public void setDescriptionVi(String descriptionVi) {
        this.descriptionVi = descriptionVi;
    }

    public List<ProductPrice> getProductPrices() {
        return productPrices;
    }

    public void setProductPrices(List<ProductPrice> productPrices) {
        this.productPrices = productPrices;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

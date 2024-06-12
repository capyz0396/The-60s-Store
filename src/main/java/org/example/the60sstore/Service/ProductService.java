package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.Product;
import org.example.the60sstore.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<Product> getAllProductByPage(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    public Page<Product> getAllProductByPageAndSort(PageRequest pageRequest, String sortType, String language) {
        if (sortType.equals("sorta") && language.equals("en")) {
            return productRepository.findAllByOrderByProductNameEnAsc(pageRequest);
        } else if (sortType.equals("sorta") && language.equals("vi")) {
            return productRepository.findAllByOrderByProductNameViAsc(pageRequest);
        } else if (sortType.equals("sortz") && language.equals("en")) {
            return productRepository.findAllByOrderByProductNameEnDesc(pageRequest);
        } else if (sortType.equals("sortz") && language.equals("vi")) {
            return productRepository.findAllByOrderByProductNameViDesc(pageRequest);
        } else if (sortType.equals("sortl")) {
            return productRepository.findAllOrderByPriceAsc(pageRequest);
        } else if (sortType.equals("sorth")) {
            return productRepository.findAllOrderByPriceDesc(pageRequest);
        }
        return productRepository.findAll(pageRequest);
    }

    public Page<Product> getAllProductByFilter(String filter, PageRequest pageRequest) {
        return productRepository.findByProductTypeEn(filter, pageRequest);
    }

    public Page<Product> getAllProductByFilterAndSort(String filter, PageRequest pageRequest, String sortType, String language) {
        if (sortType.equals("sorta") && language.equals("en")) {
            return productRepository.findByProductTypeEnOrderByProductNameEnAsc(filter, pageRequest);
        } else if (sortType.equals("sortz") && language.equals("en")) {
            return productRepository.findByProductTypeEnOrderByProductNameEnDesc(filter, pageRequest);
        } else if (sortType.equals("sorta") && language.equals("vi")) {
            return productRepository.findByProductTypeEnOrderByProductNameViAsc(filter, pageRequest);
        } else if (sortType.equals("sortz") && language.equals("vi")) {
            return productRepository.findByProductTypeEnOrderByProductNameViDesc(filter, pageRequest);
        } else if (sortType.equals("sortl")) {
            return productRepository.findByProductTypeEnOrderByPriceAsc(filter, pageRequest);
        } else if (sortType.equals("sorth")) {
            return productRepository.findByProductTypeEnOrderByPriceDesc(filter, pageRequest);
        }
        return productRepository.findByProductTypeEn(filter, pageRequest);
    }

    public Page<Product> getAllProductByKeyword(String keyword, PageRequest pageRequest, String language) {
        if (language.equals("en")) {
            return productRepository.findProductByProductNameEnContaining(keyword, pageRequest);
        } else if (language.equals("vi")){
            return productRepository.findProductByProductNameViContaining(keyword, pageRequest);
        }
        return null;
    }

    public Page<Product> getAllProductByKeywordAndSort(String keyword, PageRequest pageRequest, String sortType, String language) {
        if (sortType.equals("sorta") && language.equals("en")) {
            return productRepository.findProductByProductNameEnContainingOrderByProductNameEnAsc(keyword, pageRequest);
        } else if (sortType.equals("sortz") && language.equals("en")) {
            return productRepository.findProductByProductNameEnContainingOrderByProductNameEnDesc(keyword, pageRequest);
        } else if (sortType.equals("sorta") && language.equals("vi")) {
            return productRepository.findProductByProductNameViContainingOrderByProductNameViAsc(keyword, pageRequest);
        } else if (sortType.equals("sortz") && language.equals("vi")) {
            return productRepository.findProductByProductNameViContainingOrderByProductNameViDesc(keyword, pageRequest);
        } else if (sortType.equals("sortl") && language.equals("en")) {
            return productRepository.findProductByProductNameEnContainingOrderByPriceAsc(keyword, pageRequest);
        } else if (sortType.equals("sorth") && language.equals("en")) {
            return productRepository.findProductByProductNameEnContainingOrderByPriceDesc(keyword, pageRequest);
        } else if (sortType.equals("sortl") && language.equals("vi")) {
            return productRepository.findProductByProductNameViContainingOrderByPriceAsc(keyword, pageRequest);
        } else if (sortType.equals("sorth") && language.equals("vi")) {
            return productRepository.findProductByProductNameViContainingOrderByPriceDesc(keyword, pageRequest);
        }
        return productRepository.findAll(pageRequest);
    }
}

package com.devtam.commonbase.repository;

import com.devtam.commonbase.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getAllByCategoryId(long categoryId, Pageable pageable);

    Page<Product> findAllByProductStatus(Pageable pageable, boolean status);

    Page<Product> findAllByProductStatusOrderByProductIdDesc(Pageable pageable, boolean status);

    Page<Product> findAllByCategoryParentIdAndProductStatus(long categoryId, Pageable pageable, boolean status);

    Page<Product> findAllByCategoryParentIdAndProductStatusAndProductPriceBetween(long categoryId, boolean status, long from, long to, Pageable pageable);

    Page<Product> findAllByCategoryIdAndProductStatusOrderByProductCreatedDesc(long categoryId, Pageable pageable, boolean status);

    Page<Product> findAllByCategoryParentIdAndProductStatusOrderByProductCreatedDesc(long categoryId, boolean status, Pageable pageable);

    Page<Product> findAllByProductStatusAndProductTitleContaining(boolean status, String searchValue, Pageable pageable);

    Product getByProductId(long productId);

    @Query("SELECT p FROM Product p WHERE p.productStatus = :status ORDER BY (CASE WHEN p.productTotal = 0 THEN 0 ELSE p.productSold * 1.0 / p.productTotal END) DESC, p.productCreated DESC")
    List<Product> findAllSortedBySoldRatioAndCreated(@Param("status") boolean status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productStatus = :status ORDER BY (CASE WHEN p.productTotal = 0 THEN 0 ELSE p.productSold * 1.0 / p.productTotal END) DESC, p.productCreated DESC")
    Page<Product> findPageSortedBySoldRatioAndCreated(@Param("status") boolean status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productStatus = :status and p.categoryId = :categoryId ORDER BY (CASE WHEN p.productTotal = 0 THEN 0 ELSE p.productSold * 1.0 / p.productTotal END) DESC, p.productCreated DESC")
    Page<Product> findAllByCategoryIdSortedBySoldRatioAndCreated(@Param("categoryId") long categoryId, @Param("status") boolean status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productStatus = :status and p.categoryParentId = :categoryId ORDER BY (CASE WHEN p.productTotal = 0 THEN 0 ELSE p.productSold * 1.0 / p.productTotal END) DESC, p.productCreated DESC")
    Page<Product> findAllByCategoryParentIdSortedBySoldRatioAndCreated(@Param("categoryId") long categoryId, @Param("status") boolean status, Pageable pageable);

    Page<Product> findAllByCategoryParentIdAndProductStatusOrderByProductCreated(long categoryId, boolean status, Pageable pageable);

//    List<Product> findAllByProductStatusOrderByProductCreatedDesc(boolean status, Pageable pageable);

    Page<Product> findAllByProductStatusOrderByProductCreatedDesc(boolean status, Pageable pageable);

    Page<Product> findAllByProductStatusAndCategoryIdOrderByProductCreatedDesc(boolean status, long categoryId, Pageable pageable);

    Page<Product> findAllByProductStatusAndCategoryParentIdOrderByProductCreatedDesc(boolean status, long categoryId, Pageable pageable);
}

package com.example.buildMart.repositories;

import com.example.buildMart.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @EntityGraph(attributePaths = {"images"})
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findAllWithImagesByIdsIn(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"specifications"})
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findAllWithSpecificationsByIdsIn(@Param("ids") List<Long> ids);

    Optional<Product> findById(Long id);

    Page<Product> findByDiscountGreaterThan(Integer minDiscount, Pageable pageable);
}

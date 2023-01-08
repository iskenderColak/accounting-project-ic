package com.icolak.repository;

import com.icolak.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategoryId(Long id);
    Product findByName(String name);
}

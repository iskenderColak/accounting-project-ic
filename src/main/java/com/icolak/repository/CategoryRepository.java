package com.icolak.repository;

import com.icolak.entity.Category;
import com.icolak.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByDescriptionAndCompany(String description, Company company);
}

package com.icolak.repository;

import com.icolak.entity.Company;
import com.icolak.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findAllByCompany(Company company);

    boolean existsByUsername(String username);
}

package com.icolak.repository;

import com.icolak.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByDescription(String role);
    List<Role> findByDescriptionIsNot(String role);
}

package com.icolak.repository;

import com.icolak.entity.ClientVendor;
import com.icolak.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {
    boolean existsByClientVendorName(String clientVendorName);
    List<ClientVendor> findAllByClientVendorTypeAndCompanyIdOrderByClientVendorName(ClientVendorType type, Long companyId);
}

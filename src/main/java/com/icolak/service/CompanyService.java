package com.icolak.service;

import com.icolak.dto.CompanyDTO;

import java.util.List;

public interface CompanyService {

    CompanyDTO findById(Long id);

    List<CompanyDTO> listAllCompanies();

    void save(CompanyDTO companyDTO);

    void activateCompanyStatus(Long id);

    void deactivateCompanyStatus(Long id);

    CompanyDTO update(CompanyDTO companyDTO);

    boolean isTitleExist(String title);
}

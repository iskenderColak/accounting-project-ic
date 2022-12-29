package com.icolak.service.implementation;

import com.icolak.dto.CompanyDTO;
import com.icolak.entity.Company;
import com.icolak.enums.CompanyStatus;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.CompanyRepository;
import com.icolak.service.CompanyService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil) {
        this.companyRepository = companyRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CompanyDTO findById(Long id) {
        return mapperUtil.convert(companyRepository.findById(id).orElseThrow(), new CompanyDTO());

    }

    @Override
    public List<CompanyDTO> listAllCompanies() {
        return companyRepository.findAll(Sort.by("title")).stream()
                .filter(company -> company.getId() != 1)
                .map(company -> mapperUtil.convert(company, new CompanyDTO()))
                .sorted(Comparator.comparing(CompanyDTO::getCompanyStatus))
                .collect(Collectors.toList());
    }

    @Override
    public void save(CompanyDTO companyDTO) {

        Company company = mapperUtil.convert(companyDTO, new Company());
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
    }
}

package com.icolak.service.implementation;

import com.icolak.dto.CompanyDTO;
import com.icolak.dto.UserDTO;
import com.icolak.entity.Company;
import com.icolak.entity.User;
import com.icolak.enums.CompanyStatus;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.CompanyRepository;
import com.icolak.service.CompanyService;
import com.icolak.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil, UserService userService) {
        this.companyRepository = companyRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
    }

    @Override
    public CompanyDTO findById(Long id) {
        return mapperUtil.convert(companyRepository.findById(id).orElseThrow(), new CompanyDTO());
    }

    @Override
    public List<CompanyDTO> listAllCompanies() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO currentUserDTO = userService.findByUsername(username);
        User dbUser = mapperUtil.convert(currentUserDTO, new User());

        if (dbUser.getRole().getDescription().equals("Root User")) {
            return companyRepository.findAll(Sort.by("title")).stream()
                .filter(company -> company.getId() != 1)
                .map(company -> mapperUtil.convert(company, new CompanyDTO()))
                .sorted(Comparator.comparing(CompanyDTO::getCompanyStatus))
                .collect(Collectors.toList());
        }

        return companyRepository.findAll().stream()
                .filter(company -> dbUser.getCompany().getTitle().equals(company.getTitle()))
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

    @Override
    public void activateCompanyStatus(Long id) {

        Company company = mapperUtil.convert(findById(id), new Company());
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        userService.makeUserEnableByCompany(company);
        companyRepository.save(company);
    }

    @Override
    public void deactivateCompanyStatus(Long id) {

        Company company = mapperUtil.convert(findById(id), new Company());
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        userService.makeUserDisableByCompany(company);
        companyRepository.save(company);
    }

    @Override
    public CompanyDTO update(CompanyDTO companyDTO) {

        Company dbCompany = companyRepository.findById(companyDTO.getId()).orElseThrow();
        Company convertedCompany = mapperUtil.convert(companyDTO, new Company());
        convertedCompany.setCompanyStatus(dbCompany.getCompanyStatus());
        companyRepository.save(convertedCompany);

        return findById(companyDTO.getId());
    }

    @Override
    public boolean isTitleExist(String title) {

        return companyRepository.existsByTitle(title);
    }

    @Override
    public boolean isTitleExistExceptCurrentCompanyTitle(CompanyDTO companyDTO) {

        Company company = mapperUtil.convert(findById(companyDTO.getId()), new Company());

        if (company.getTitle().equals(companyDTO.getTitle())) {
            return false;
        }

        return companyRepository.existsByTitle(companyDTO.getTitle());
    }
}

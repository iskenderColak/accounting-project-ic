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

    @Override
    public void activateCompanyStatus(Long id) {

        Company company = mapperUtil.convert(findById(id), new Company());
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
    }

    @Override
    public void deactivateCompanyStatus(Long id) {


        Company company = mapperUtil.convert(findById(id), new Company());
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        List<User> userList = userService.listAllUsers().stream()
                .map(userDTO -> mapperUtil.convert(userDTO, new User()))
                .filter(user -> user.getCompany().equals(company))
                .collect(Collectors.toList());
        userList.forEach(user -> user.setEnabled(false));
        userList.forEach(user -> userService.save(mapperUtil.convert(user, new UserDTO())));
        companyRepository.save(company);
    }

    @Override
    public CompanyDTO update(CompanyDTO companyDTO) {

        Company dbCompany = companyRepository.findById(companyDTO.getId()).orElseThrow();
        Company convertedCompany = mapperUtil.convert(companyDTO, new Company());
        convertedCompany.setCompanyStatus(dbCompany.getCompanyStatus());
        //convertedCompany.setAddress(dbCompany.getAddress());
        companyRepository.save(convertedCompany);

        return findById(companyDTO.getId());
    }
}

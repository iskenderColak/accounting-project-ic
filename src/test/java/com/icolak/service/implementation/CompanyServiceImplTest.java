package com.icolak.service.implementation;

import com.icolak.dto.CompanyDTO;
import com.icolak.entity.Company;
import com.icolak.enums.CompanyStatus;
import com.icolak.exception.CompanyNotFoundException;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.CompanyRepository;
import com.icolak.testDocumentInitializer.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    CompanyRepository companyRepository;
    @Mock
    UserServiceImpl userService;
    @InjectMocks
    CompanyServiceImpl companyService;
    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Test
    @DisplayName("Testing findById()")
    void testFindById() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(new Company()));

        CompanyDTO companyDTO = companyService.findById(anyLong());

        InOrder inOrder = inOrder(companyRepository, mapperUtil);
        inOrder.verify(companyRepository).findById(anyLong());
        inOrder.verify(mapperUtil).convert(any(Company.class), any(CompanyDTO.class));
        Assertions.assertTrue(companyService.findById(anyLong()) instanceof CompanyDTO);
        assertNotNull(companyDTO);

    }

    @Test
    @DisplayName("When company is searched with non-existing company id, " +
            "it should throw CompanyNotFoundException")
    void testFindById_Throws() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CompanyNotFoundException.class, () -> companyService.findById(anyLong()));
    }

    @Test
    @DisplayName("When company save() method is called, company should be saved and the titles of " +
            "saved company and returning company should be equal")
    void testSave() {
        CompanyDTO companyDTO = TestConstants.getTestCompanyDTO(CompanyStatus.PASSIVE);
        Company company = mapperUtil.convert(companyDTO, new Company());

        company.setTitle(TestConstants.SAMPLE_COMPANY1);
        given(companyRepository.save(any())).willReturn(company);

        var resultCompany = companyService.save(companyDTO);

        assertEquals(TestConstants.SAMPLE_COMPANY1, resultCompany.getTitle());
        // Since when creating a new company we assign status as ACTIVE, the status should be active
        assertEquals(CompanyStatus.ACTIVE, resultCompany.getCompanyStatus());
        verify(companyRepository).save(any());
    }

    @Test
    @DisplayName("When a company is activated its status should be ACTIVE")
    void testActivateCompanyStatus() {
        Company company = TestConstants.getTestCompany(CompanyStatus.PASSIVE);
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));

        companyService.activateCompanyStatus(company.getId());
        userService.makeUserEnableByCompany(company);

        InOrder inOrder = inOrder(userService, companyRepository);
        inOrder.verify(userService).makeUserEnableByCompany(any());
        inOrder.verify(companyRepository).save(any());
        assertEquals(company.getCompanyStatus(), CompanyStatus.ACTIVE);
    }

    @Test
    @DisplayName("When a company is deactivated its status should be PASSIVE")
    void testDeactivateCompanyStatus() {
        Company company = TestConstants.getTestCompany(CompanyStatus.ACTIVE);
        when(companyRepository.findById(TestConstants.SAMPLE_ID1)).thenReturn(Optional.of(company));

        companyService.deactivateCompanyStatus(company.getId());
        userService.makeUserDisableByCompany(company);

        InOrder inOrder = inOrder(userService, companyRepository);
        inOrder.verify(userService).makeUserDisableByCompany(any());
        inOrder.verify(companyRepository).save(any());
        assertEquals(company.getCompanyStatus(), CompanyStatus.PASSIVE);
    }

    @Test
    @DisplayName("When updating the company should be saved and its status should be as in db as well")
    void testUpdate() {
        CompanyDTO companyDTO = TestConstants.getTestCompanyDTO(CompanyStatus.PASSIVE);
        Company dbCompany = new Company();
        dbCompany.setCompanyStatus(CompanyStatus.ACTIVE);

        when(companyRepository.findById(companyDTO.getId())).thenReturn(Optional.of(dbCompany));

        // When we called update() method, save method should be called 1 time as well
        var updatedDTO = companyService.update(companyDTO);
        verify(companyRepository, times(1)).save(any());

        // When updating the company, the status of dbCompany and updateCompany should be equal,
        // as we assign the value we have in the db to company status.
        assertEquals(updatedDTO.getCompanyStatus(), dbCompany.getCompanyStatus());
        // Since we don't change website, it should be equal to before updating value
        assertEquals(updatedDTO.getWebsite(), companyDTO.getWebsite());
    }

    @Test
    void testIsTitleExist() {
        companyService.isTitleExist(anyString());
        verify(companyRepository, times(1)).existsByTitle(anyString());
/*
        Company company = TestConstants.getTestCompany(CompanyStatus.ACTIVE);
        when(companyRepository.save(company)).thenReturn(company);
        Company savedCompany = companyRepository.save(company);
        assertTrue(companyService.isTitleExist(savedCompany.getTitle()));

*/
    }
}
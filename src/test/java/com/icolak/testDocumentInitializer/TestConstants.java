package com.icolak.testDocumentInitializer;

import com.icolak.dto.*;
import com.icolak.entity.Address;
import com.icolak.entity.Company;
import com.icolak.entity.Role;
import com.icolak.entity.User;
import com.icolak.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class TestConstants {

    public static final String PASSWORD_CLEAR_ABC1 = "Abc1";
    public static final String PASSWORD_ENCODED_ABC1 = "$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK";
    public static final String SAMPLE_FIRST_NAME_JOHN = "John";
    public static final String SAMPLE_FIRST_NAME_ADAM = "Adam";
    public static final String SAMPLE_LAST_NAME_JOHN = "Smith";
    public static final String SAMPLE_PHONE_NUMBER1 = "+1 (111) 111-1111";
    public static final String SAMPLE_COMPANY1 = "Test Company 1";
    public static final String SAMPLE_CATEGORY1 = "Test Category 1";
    public static final String SAMPLE_PRODUCT1 = "Test Product 1";
    public static final String SAMPLE_PRODUCT2 = "Test Product 2";
    public static final String SAMPLE_CLIENT1 = "Test Client 1";
    public static final String SAMPLE_VENDOR1 = "Test Vendor 1";
    public static final String SAMPLE_EMAIL1 = "abc@def.com";
    public static final String SAMPLE_EMAIL2 = "def@ghi.com";
    public static final int SAMPLE_TAX_RATE10 = 10;
    public static final int SAMPLE_QUANTITY10 = 10;
    public static final int SAMPLE_QUANTITY_IN_STOCK_10 = 10;
    public static final int SAMPLE_LOW_LIMIT_ALERT5 = 5;
    public static final String SAMPLE_PURCHASE_INVOICE_NO1 = "P-001";
    public static final String SAMPLE_SALES_INVOICE_NO1 = "S-001";
    public static final String SAMPLE_WEB_SITE1 = "https://www.test.com";
    public static final BigDecimal SAMPLE_PRICE1000 = BigDecimal.valueOf(1000);
    public static final LocalDate SAMPLE_DATE_2022_1_1 = LocalDate.of(2022, 01, 01);
    public static final Long SAMPLE_ID1 = 1L;

    public static User getTestUser(RoleEnum roleEnum) {
        return User.builder()
                .id(SAMPLE_ID1)
                .firstname(SAMPLE_FIRST_NAME_JOHN)
                .lastname(SAMPLE_LAST_NAME_JOHN)
                .phone(SAMPLE_PHONE_NUMBER1)
                .password(PASSWORD_CLEAR_ABC1)
                .role(getTestRole())
                .company(getTestCompany(CompanyStatus.ACTIVE))
                .build();
    }

    public static Role getTestRole() {
        return Role.builder()
                .description(RoleEnum.ADMIN.getValue())
                .build();
    }

    public static Company getTestCompany(CompanyStatus status) {
        return Company.builder()
                .title(SAMPLE_COMPANY1)
                .website(SAMPLE_WEB_SITE1)
                .id(SAMPLE_ID1)
                .phone(SAMPLE_PHONE_NUMBER1)
                .companyStatus(status)
                .address(new Address())
                .build();
    }

    public static UserDTO getTestUserDTO(RoleEnum roleEnum) {
        return UserDTO.builder()
                .id(SAMPLE_ID1)
                .firstname(SAMPLE_FIRST_NAME_JOHN)
                .lastname(SAMPLE_LAST_NAME_JOHN)
                .phone(SAMPLE_PHONE_NUMBER1)
                .password(PASSWORD_CLEAR_ABC1)
                .confirmPassword(PASSWORD_CLEAR_ABC1)
                .role(new RoleDTO(SAMPLE_ID1, roleEnum.getValue()))
                .isOnlyAdmin(false)
                .company(getTestCompanyDTO(CompanyStatus.ACTIVE))
                .build();
    }

    public static CompanyDTO getTestCompanyDTO(CompanyStatus status) {
        return CompanyDTO.builder()
                .title(SAMPLE_COMPANY1)
                .website(SAMPLE_WEB_SITE1)
                .id(SAMPLE_ID1)
                .phone(SAMPLE_PHONE_NUMBER1)
                .companyStatus(status)
                .address(new AddressDTO())
                .build();
    }

    public static CategoryDTO getTestCategoryDTO() {
        return CategoryDTO.builder()
                .company(getTestCompanyDTO(CompanyStatus.ACTIVE))
                .description(SAMPLE_CATEGORY1)
                .build();
    }

    public static ClientVendorDTO getTestClientVendorDTO(ClientVendorType type) {
        return ClientVendorDTO.builder()
                .clientVendorType(type)
                .clientVendorName(SAMPLE_CLIENT1)
                .address(new AddressDTO())
                .website(SAMPLE_WEB_SITE1)
                .phone(SAMPLE_PHONE_NUMBER1)
                .build();
    }

    public static ProductDTO getTestProductDTO() {
        return ProductDTO.builder()
                .category(getTestCategoryDTO())
                .productUnit(ProductUnit.PCS)
                .name(SAMPLE_PRODUCT1)
                .quantityInStock(SAMPLE_QUANTITY_IN_STOCK_10)
                .lowLimitAlert(SAMPLE_LOW_LIMIT_ALERT5)
                .build();
    }

    public static InvoiceProductDTO getTestInvoiceProductDTO() {
        return InvoiceProductDTO.builder()
                .product(getTestProductDTO())
                .price(BigDecimal.TEN)
                .tax(SAMPLE_TAX_RATE10)
                .quantity(SAMPLE_QUANTITY10)
                .invoice(new InvoiceDTO())
                .build();
    }

    public static InvoiceDTO getTestInvoiceDTO(InvoiceStatus status, InvoiceType type) {
        return InvoiceDTO.builder()
                .invoiceNo(type == InvoiceType.PURCHASE ? SAMPLE_PURCHASE_INVOICE_NO1 : SAMPLE_SALES_INVOICE_NO1)
                .clientVendor(getTestClientVendorDTO(ClientVendorType.CLIENT))
                .invoiceStatus(status)
                .invoiceType(type)
                .date(SAMPLE_DATE_2022_1_1)
                .company(getTestCompanyDTO(CompanyStatus.ACTIVE))
                .invoiceProducts(new ArrayList<>(Arrays.asList(getTestInvoiceProductDTO())))
                .price(SAMPLE_PRICE1000)
                .tax(BigDecimal.TEN)
                .total(BigDecimal.TEN.multiply(SAMPLE_PRICE1000))
                .build();
    }

}
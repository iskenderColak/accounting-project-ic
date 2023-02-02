package com.icolak.service.implementation;

import com.icolak.dto.InvoiceDTO;
import com.icolak.dto.InvoiceProductDTO;
import com.icolak.entity.InvoiceProduct;
import com.icolak.enums.InvoiceStatus;
import com.icolak.enums.InvoiceType;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.InvoiceProductRepository;
import com.icolak.service.InvoiceProductService;
import com.icolak.service.InvoiceService;
import com.icolak.service.SecurityService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;
    private final InvoiceService invoiceService;
    private final SecurityService securityService;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil, @Lazy InvoiceService invoiceService, SecurityService securityService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
        this.invoiceService = invoiceService;
        this.securityService = securityService;
    }

    @Override
    public InvoiceProductDTO findById(Long id) {
        return mapperUtil.convert(invoiceProductRepository.findById(id).orElseThrow(), new InvoiceProductDTO());
    }

    @Override
    public boolean isExistByProductId(Long id) {
        return invoiceProductRepository.existsByProductId(id);
    }

    @Override
    public List<InvoiceProductDTO> listByInvoiceId(Long id) {
        return invoiceProductRepository.findAllByInvoiceId(id).stream()
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceProductDTO save(InvoiceProductDTO invoiceProductDTO, Long id) {
        invoiceProductDTO.setProfitLoss(BigDecimal.ZERO);
        InvoiceDTO invoiceDTO = invoiceService.findById(id);
        invoiceProductDTO.setInvoice(invoiceDTO);
        invoiceProductDTO.setRemainingQuantity(0);
        InvoiceProduct invoiceProduct = invoiceProductRepository.save(mapperUtil.convert(invoiceProductDTO, new InvoiceProduct()));
        return mapperUtil.convert(invoiceProduct, new InvoiceProductDTO());
    }

    @Override
    public BigDecimal getTotalPriceWithTaxByInvoice(Long invoiceId) {
        return calculatePriceWithTax(invoiceProductRepository.findByInvoice_IdAndInvoice_Company_Id(invoiceId, currentCompanyId()));
    }

    @Override
    public BigDecimal getTotalPriceWithoutTaxByInvoice(Long invoiceId) {
        return invoiceProductRepository.findByInvoice_IdAndInvoice_Company_Id(invoiceId, currentCompanyId())
                .stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                        .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public void deleteInvoiceProductById(Long invoiceProductId) {
        invoiceProductRepository.delete(mapperUtil.convert(findById(invoiceProductId), new InvoiceProduct()));
    }

    @Override
    public void deleteRelatedInvoiceProducts(Long invoiceId) {
        listByInvoiceId(invoiceId)
                .forEach(dto -> {
                    InvoiceProduct invoiceProduct = mapperUtil.convert(dto, new InvoiceProduct());
                    invoiceProduct.setIsDeleted(true);
                    invoiceProductRepository.save(invoiceProduct);
                });
    }

    @Override
    public BigDecimal getTotalSalesForCurrentCompany() {
        return calculatePriceWithTax(invoiceProductRepository
                .findAllByInvoiceCompanyIdAndInvoiceInvoiceTypeAndInvoiceInvoiceStatus(currentCompanyId(), InvoiceType.SALES, InvoiceStatus.APPROVED));
    }

    @Override
    public BigDecimal getTotalCostForCurrentCompany() {
        return calculatePriceWithTax(invoiceProductRepository
                .findAllByInvoiceCompanyIdAndInvoiceInvoiceTypeAndInvoiceInvoiceStatus(currentCompanyId(), InvoiceType.PURCHASE, InvoiceStatus.APPROVED));
    }

    @Override
    public BigDecimal getTotalProfitLossForCurrentCompany() {
        //   return getTotalSalesForCurrentCompany().subtract(getTotalCostForCurrentCompany());
        return invoiceProductRepository.findAllByInvoiceId(currentCompanyId())
                .stream()
                .map(InvoiceProduct::getProfitLoss)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public List<InvoiceProductDTO> listPurchaseInvoiceProductIncludesProductsOfSalesInvoiceProduct(Long productId) {
        return invoiceProductRepository
                .findAllByProductIdAndRemainingQuantityGreaterThanZeroAndPurchaseApprovedInvoiceOrderByLastUpdateDateTime(productId)
                .stream()
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveSettingsAfterApproving(InvoiceProductDTO invoiceProductDTO) {
        invoiceProductRepository.save(mapperUtil.convert(invoiceProductDTO, new InvoiceProduct()));
    }

    @Override
    public List<InvoiceProductDTO> getAllApprovedInvoicesForCurrentCompany() {
        return null;
    }

    private Long currentCompanyId() {
        return securityService.getLoggedInUser().getCompany().getId();
    }

    private BigDecimal calculatePriceWithTax(List<InvoiceProduct> list) {
        return list.stream()
                //.map(invoiceProduct -> calculatePriceWithTax(invoiceProduct))
                .map(this::calculatePriceWithTax)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculatePriceWithTax(InvoiceProduct invoiceProduct) {
        return invoiceProduct.getPrice()
                .add(invoiceProduct.getPrice()
                        .multiply(BigDecimal.valueOf(invoiceProduct.getTax()))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN))
                .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity()));
    }
}

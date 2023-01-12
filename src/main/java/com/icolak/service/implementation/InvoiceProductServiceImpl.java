package com.icolak.service.implementation;

import com.icolak.dto.InvoiceDTO;
import com.icolak.dto.InvoiceProductDTO;
import com.icolak.entity.InvoiceProduct;
import com.icolak.entity.Product;
import com.icolak.enums.InvoiceStatus;
import com.icolak.enums.InvoiceType;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.InvoiceProductRepository;
import com.icolak.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil, @Lazy InvoiceService invoiceService, SecurityService securityService, ProductRepository productRepository) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
        this.invoiceService = invoiceService;
        this.securityService = securityService;
        this.productRepository = productRepository;
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
    public void save(InvoiceProductDTO invoiceProductDTO, Long id) {
        invoiceProductDTO.setProfitLoss(invoiceProductDTO.getTotal()); // TODO: Take a look
        InvoiceDTO invoiceDTO = invoiceService.findById(id);
        invoiceProductDTO.setInvoice(invoiceDTO);
        invoiceProductDTO.setRemainingQuantity(0);
        invoiceProductRepository.save(mapperUtil.convert(invoiceProductDTO, new InvoiceProduct()));
    }
/*
    @Override
    public void saveAfterCheckingStock(InvoiceProductDTO invoiceProductDTO, Long invoiceId) throws IllegalAccessException {
        if (checkAndSetTheStockOfProduct(invoiceProductDTO.getProduct().getId(), invoiceProductDTO.getQuantity())) {
            invoiceProductDTO.setQuantity(invoiceProductDTO.getQuantity());
        } else {
            throw new IllegalAccessException("Stock is not enogh !!!");
        }
        invoiceProductDTO.setProfitLoss(invoiceProductDTO.getTotal());
        InvoiceDTO invoiceDTO = invoiceService.findById(invoiceId);
        invoiceProductDTO.setInvoice(invoiceDTO);
        invoiceProductDTO.setRemainingQuantity(0); // TODO: 7.01.2023
        invoiceProductRepository.save(mapperUtil.convert(invoiceProductDTO, new InvoiceProduct()));
    }

    private boolean checkAndSetTheStockOfProduct(Long productId, Integer quantity) {
        List<InvoiceProduct> list = invoiceProductRepository
                .findAllByProductIdAndInvoiceInvoiceTypeAndInvoiceInvoiceStatusOrderByInvoiceDate(productId, InvoiceType.PURCHASE, InvoiceStatus.APPROVED);
        Integer totalStockOfProduct = list.stream().map(InvoiceProduct::getRemainingQuantity).reduce(Integer::sum).get();
        return totalStockOfProduct >= quantity;
    }

 */

    @Override
    public boolean isStockEnough(InvoiceProductDTO invoiceProductDTO) {
        int totalStock = productRepository.findByName(invoiceProductDTO.getProduct().getName()).getQuantityInStock();
        return totalStock >= invoiceProductDTO.getQuantity();
    }

    @Override
    public BigDecimal getTotalPriceWithTaxByInvoice(String invoiceNo) {
        return calculatePriceWithTax(invoiceProductRepository.findByInvoice_InvoiceNoAndInvoice_Company_Id(invoiceNo, currentCompanyId()));
    }

    @Override
    public BigDecimal getTotalPriceWithoutTaxByInvoice(String invoiceNo) {
        return invoiceProductRepository.findByInvoice_InvoiceNoAndInvoice_Company_Id(invoiceNo, currentCompanyId())
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
    public void setQuantitiesAfterApprovePurchaseInvoice(List<InvoiceProductDTO> invoiceProductDTOList) {
        invoiceProductDTOList
                .forEach(dto -> {
                    InvoiceProduct entity = invoiceProductRepository.findById(dto.getId()).orElseThrow();
                    entity.setProfitLoss(calculatePriceWithTax(entity));
                    entity.setRemainingQuantity(entity.getRemainingQuantity() + entity.getQuantity()); // TODO: check if needed
                    Product product = productRepository.findByName(entity.getProduct().getName());
                    product.setQuantityInStock(product.getQuantityInStock()+entity.getQuantity());
                    invoiceProductRepository.save(entity);
                });
    }

    private Long currentCompanyId() {
        return securityService.getLoggedInUser().getCompany().getId();
    }

    private BigDecimal calculatePriceWithTax(List<InvoiceProduct> list) {
        return list.stream()
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

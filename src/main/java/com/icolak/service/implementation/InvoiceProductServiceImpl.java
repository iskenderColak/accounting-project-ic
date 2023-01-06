package com.icolak.service.implementation;

import com.icolak.dto.InvoiceDTO;
import com.icolak.dto.InvoiceProductDTO;
import com.icolak.entity.InvoiceProduct;
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
    public void save(InvoiceProductDTO invoiceProductDTO, Long id) {
        invoiceProductDTO.setProfitLoss(BigDecimal.ZERO);//required calc
        InvoiceDTO invoiceDTO = invoiceService.findById(id);
        invoiceProductDTO.setInvoice(invoiceDTO);
        invoiceProductDTO.setRemainingQuantity(invoiceProductDTO.getQuantity());
        invoiceProductRepository.save(mapperUtil.convert(invoiceProductDTO, new InvoiceProduct()));
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

    private Long currentCompanyId() {
        return securityService.getLoggedInUser().getCompany().getId();
    }

    private BigDecimal calculatePriceWithTax(List<InvoiceProduct> list) {
        return list.stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                        .add(invoiceProduct.getPrice()
                                .multiply(BigDecimal.valueOf(invoiceProduct.getTax()))
                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN))
                        .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}

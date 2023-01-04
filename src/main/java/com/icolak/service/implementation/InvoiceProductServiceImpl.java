package com.icolak.service.implementation;

import com.icolak.dto.InvoiceProductDTO;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.InvoiceProductRepository;
import com.icolak.service.InvoiceProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public boolean isExistByProductId(Long id) {
        return invoiceProductRepository.existsByProductId(id);
    }

    @Override
    public List<InvoiceProductDTO> listByInvoiceId(Long id) {
        return invoiceProductRepository.findAllByInvoiceId(id).stream()
                .map(invoiceProduct -> {
                    InvoiceProductDTO dto = mapperUtil.convert(invoiceProduct, new InvoiceProductDTO());
                    dto.setTotal(invoiceProduct.getPrice()
                            .add(invoiceProduct.getPrice()
                                    .multiply(BigDecimal.valueOf(invoiceProduct.getTax()))
                                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN))
                            .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalPriceWithTaxByInvoiceId(Long id) {
        return listByInvoiceId(id).stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                    .add(invoiceProduct.getPrice()
                            .multiply(BigDecimal.valueOf(invoiceProduct.getTax()))
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN))
                    .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow();
    }

    @Override
    public BigDecimal getTotalPriceWithoutTaxByInvoiceId(Long id) {
        return listByInvoiceId(id).stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                        .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow();
    }
}

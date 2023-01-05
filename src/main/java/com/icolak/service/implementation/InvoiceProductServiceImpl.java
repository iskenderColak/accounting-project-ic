package com.icolak.service.implementation;

import com.icolak.dto.InvoiceDTO;
import com.icolak.dto.InvoiceProductDTO;
import com.icolak.entity.InvoiceProduct;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.InvoiceProductRepository;
import com.icolak.service.InvoiceProductService;
import com.icolak.service.InvoiceService;
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

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil, @Lazy InvoiceService invoiceService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
        this.invoiceService = invoiceService;
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

    @Override
    public void save(InvoiceProductDTO invoiceProductDTO, Long id) {
        invoiceProductDTO.setProfitLoss(BigDecimal.ZERO);//required calc
        InvoiceDTO invoiceDTO = invoiceService.findById(id);
        invoiceProductDTO.setInvoice(invoiceDTO);
        //invoiceDTO.getInvoiceProducts().add(invoiceProductDTO);
        invoiceProductDTO.setRemainingQuantity(invoiceProductDTO.getQuantity());
        invoiceProductRepository.save(mapperUtil.convert(invoiceProductDTO, new InvoiceProduct()));
    }
}

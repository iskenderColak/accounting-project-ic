package com.icolak.service.implementation;

import com.icolak.dto.InvoiceDTO;
import com.icolak.dto.InvoiceProductDTO;
import com.icolak.dto.ProductDTO;
import com.icolak.entity.Invoice;
import com.icolak.entity.InvoiceProduct;
import com.icolak.enums.ClientVendorType;
import com.icolak.enums.InvoiceStatus;
import com.icolak.enums.InvoiceType;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.InvoiceRepository;
import com.icolak.service.InvoiceProductService;
import com.icolak.service.InvoiceService;
import com.icolak.service.ProductService;
import com.icolak.service.SecurityService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapperUtil;
    private final InvoiceProductService invoiceProductService;
    private final SecurityService securityService;
    private final ProductService productService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapperUtil, InvoiceProductService invoiceProductService, SecurityService securityService, ProductService productService) {
        this.invoiceRepository = invoiceRepository;
        this.mapperUtil = mapperUtil;
        this.invoiceProductService = invoiceProductService;
        this.securityService = securityService;
        this.productService = productService;
    }

    @Override
    public InvoiceDTO findById(Long id) {
        return mapperUtil.convert(invoiceRepository.findById(id).orElseThrow(), new InvoiceDTO());
    }

    @Override
    public List<InvoiceDTO> listAllInvoicesByTypeAndCompany(InvoiceType invoiceType) {
        return setPriceTaxTotalToInvoice(invoiceRepository.findAllByInvoiceTypeAndCompanyIdOrderByLastUpdateDateTimeDesc(invoiceType, currentCompanyId()));
    }

    @Override
    public boolean existsByClientVendorId(Long id) {
        return invoiceRepository.existsByClientVendorId(id);
    }

    @Override
    public String generateInvoiceNo(InvoiceType invoiceType) {
        Invoice invoice = invoiceRepository.findTopByCompanyIdAndInvoiceTypeOrderByIdDesc(
                securityService.getLoggedInUser().getCompany().getId(), invoiceType);
        if (invoice == null) {
            if (invoiceType.equals(InvoiceType.PURCHASE)) {
                return "P-001";
            } else {
                return "S-001";
            }
        }
        int number = Integer.parseInt(invoice.getInvoiceNo().substring(2)) + 1;
        String prefix;
        if (invoice.getInvoiceType().equals(InvoiceType.PURCHASE)) {
            prefix = "P-";
        } else {
            prefix = "S-";
        }
        if (number < 10) {
            return prefix + "00" + number;
        } else if (number < 100) {
            return prefix + "0" + number;
        }
        return prefix + number;
    }

    @Override
    public void save(InvoiceDTO invoiceDto) {
        if (invoiceDto.getClientVendor().getClientVendorType().equals(ClientVendorType.CLIENT)) {
            invoiceDto.setInvoiceType(InvoiceType.SALES);
        } else {
            invoiceDto.setInvoiceType(InvoiceType.PURCHASE);
        }

        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        invoiceRepository.save(invoice);
        invoiceDto.setId(invoice.getId());
    }

    @Override
    public InvoiceDTO update(InvoiceDTO invoiceDTO) {
        Invoice dbInvoice = invoiceRepository.findById(invoiceDTO.getId()).orElseThrow();
        Invoice convertedInvoice = mapperUtil.convert(invoiceDTO, new Invoice());
        convertedInvoice.setInvoiceStatus(dbInvoice.getInvoiceStatus());
        convertedInvoice.setInvoiceType(dbInvoice.getInvoiceType());
        convertedInvoice.setCompany(dbInvoice.getCompany());
        convertedInvoice.setDate(LocalDate.now());
        invoiceRepository.save(convertedInvoice);
        return findById(invoiceDTO.getId());
    }

    @Override
    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setIsDeleted(true);
        invoiceProductService.deleteRelatedInvoiceProducts(id);
        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceDTO> listLast3ApprovedInvoicesByCompany() {
        return setPriceTaxTotalToInvoice(invoiceRepository
                .findTop3ByCompanyIdAndInvoiceStatusOrderByDateDesc(currentCompanyId(), InvoiceStatus.APPROVED));
    }

    @Override
    public void approvePurchaseInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        List<InvoiceProduct> list = invoice.getInvoiceProducts();
        list.forEach(entity -> {
            entity.setProfitLoss(calculatePriceWithTax(entity));
            entity.setRemainingQuantity(entity.getQuantity());
            ProductDTO productDTO = productService.findByName(entity.getProduct().getName());
            productDTO.setQuantityInStock(productDTO.getQuantityInStock() + entity.getRemainingQuantity());
            productService.save(productDTO);
        });
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);
    }

    private BigDecimal calculatePriceWithTax(InvoiceProduct invoiceProduct) {
        return invoiceProduct.getPrice()
                .add(invoiceProduct.getPrice()
                        .multiply(BigDecimal.valueOf(invoiceProduct.getTax()))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN))
                .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity()));
    }

    @Override
    public void approveSalesInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        List<InvoiceProduct> salesInvoiceProductList = invoice.getInvoiceProducts();
        salesInvoiceProductList.forEach(salesInvoiceProduct -> {
            List<InvoiceProduct> purchaseInvoiceProductList = entityMapper(salesInvoiceProduct.getProduct().getId());
            int quantitySold = salesInvoiceProduct.getQuantity();
            int purchaseInvoiceProductIndex = 0;
            BigDecimal totalCost = BigDecimal.ZERO;
            while (quantitySold > 0) {
                InvoiceProduct currentPurchaseInvoiceProduct = purchaseInvoiceProductList.get(purchaseInvoiceProductIndex);
                int remainingQuantitySold = quantitySold - currentPurchaseInvoiceProduct.getRemainingQuantity();
                if (remainingQuantitySold > 0) {
                    totalCost = totalCost.add(getTotalWithTax(currentPurchaseInvoiceProduct, currentPurchaseInvoiceProduct.getRemainingQuantity()));
                    purchaseInvoiceProductIndex ++;
                    quantitySold = remainingQuantitySold;
                    currentPurchaseInvoiceProduct.setRemainingQuantity(0);
                } else {
                    totalCost = totalCost.add(getTotalWithTax(currentPurchaseInvoiceProduct, quantitySold));
                    currentPurchaseInvoiceProduct.setRemainingQuantity(currentPurchaseInvoiceProduct.getRemainingQuantity()-quantitySold);
                    quantitySold -= currentPurchaseInvoiceProduct.getRemainingQuantity();
                }
                invoiceProductService.saveSettingsAfterApproving(mapperUtil.convert(currentPurchaseInvoiceProduct, new InvoiceProductDTO()));
            }
            salesInvoiceProduct.setProfitLoss(getTotalWithTax(salesInvoiceProduct, salesInvoiceProduct.getQuantity()).subtract(totalCost));
            invoiceProductService.saveSettingsAfterApproving(mapperUtil.convert(salesInvoiceProduct, new InvoiceProductDTO()));
            productService.setStockAfterSelling(salesInvoiceProduct.getProduct().getId(), salesInvoiceProduct.getQuantity());
        });
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);
    }

    private BigDecimal getTotalWithTax(InvoiceProduct invoiceProduct, int remainingQuantity) {
        BigDecimal beforeTax = BigDecimal.valueOf(remainingQuantity).multiply(invoiceProduct.getPrice());
        BigDecimal taxValue = BigDecimal.valueOf(invoiceProduct.getTax()).multiply(beforeTax).divide(BigDecimal.valueOf(100L));
        return beforeTax.add(taxValue);
    }

    private List<InvoiceProduct> entityMapper(Long productId) {
        return invoiceProductService
                .listPurchaseInvoiceProductIncludesProductsOfSalesInvoiceProduct(productId)
                .stream()
                .map(invoiceProductDTO -> mapperUtil.convert(invoiceProductDTO, new InvoiceProduct()))
                .collect(Collectors.toList());
    }

    private List<InvoiceDTO> setPriceTaxTotalToInvoice(List<Invoice> list) {
        return list.stream()
                .map(invoice -> {
                    InvoiceDTO dto = mapperUtil.convert(invoice, new InvoiceDTO());
                    dto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoice.getId()));
                    dto.setPrice(invoiceProductService.getTotalPriceWithoutTaxByInvoice(invoice.getId()));
                    dto.setTax(dto.getTotal().subtract(dto.getPrice()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Long currentCompanyId() {
        return securityService.getLoggedInUser().getCompany().getId();
    }
}

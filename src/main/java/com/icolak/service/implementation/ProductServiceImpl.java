package com.icolak.service.implementation;

import com.icolak.dto.InvoiceProductDTO;
import com.icolak.dto.ProductDTO;
import com.icolak.entity.Product;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.ProductRepository;
import com.icolak.service.InvoiceProductService;
import com.icolak.service.ProductService;
import com.icolak.service.SecurityService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private  final InvoiceProductService invoiceProductService;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, SecurityService securityService, @Lazy InvoiceProductService invoiceProductService) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public ProductDTO findById(Long id) {
        return mapperUtil.convert(productRepository.findById(id).orElseThrow(), new ProductDTO());
    }

    @Override
    public List<ProductDTO> listAllProductsByCompany() {
        return productRepository.findAll(Sort.by("category", "name")).stream()
                .filter(product -> product.getCategory().getCompany().getId().equals(securityService.getLoggedInUser().getCompany().getId()))
                .map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isStockEnough(InvoiceProductDTO invoiceProductDTO) {
        int remainingStock = productRepository.findById(invoiceProductDTO.getProduct().getId()).get().getQuantityInStock();
        return remainingStock >= invoiceProductDTO.getQuantity();
    }

    @Override
    public ProductDTO findByName(String name) {
        return mapperUtil.convert(productRepository.findByName(name), new ProductDTO());
    }

    @Override
    public boolean isExistByCategoryId(Long id) {
        return productRepository.existsByCategoryId(id);
    }

    @Override
    public void save(ProductDTO productDTO) {
        Product product = mapperUtil.convert(productDTO, new Product());
        productRepository.save(product);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        Product dbProduct = productRepository.findById(productDTO.getId()).orElseThrow();
        Product convertedProduct = mapperUtil.convert(productDTO, new Product());
        convertedProduct.setQuantityInStock(dbProduct.getQuantityInStock());
        productRepository.save(convertedProduct);
        return findById(productDTO.getId());
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        if (product.getQuantityInStock() == 0 && !invoiceProductService.isExistByProductId(id)) {
            product.setIsDeleted(true);
            productRepository.save(product);
        }
    }
}

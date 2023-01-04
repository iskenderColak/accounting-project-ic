package com.icolak.dto;

import com.icolak.enums.InvoiceStatus;
import com.icolak.enums.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {

    private Long id;
    private String invoiceNo;
    private InvoiceStatus invoiceStatus;
    private InvoiceType invoiceType;

    @NotNull
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;

    @NotNull(message = "Required field")
    @Valid
    private ClientVendorDTO clientVendor;

    private CompanyDTO company;
    private BigDecimal price;
    private BigDecimal tax;
    private BigDecimal total;
    private List<InvoiceProductDTO> invoiceProducts;

}

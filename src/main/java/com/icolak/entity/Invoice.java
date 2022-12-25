package com.icolak.entity;

import com.icolak.enums.InvoiceStatus;
import com.icolak.enums.InvoiceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "invoices")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
public class Invoice extends BaseEntity {

    private String invoiceNo;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "client_vendor_id")
    private ClientVendor clientVendor;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
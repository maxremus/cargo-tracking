package org.example.cargotracking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomingLoadSearchDTO {

    private String supplierCompany;

    private String invoiceNumber;

    private String productName;

    private LocalDate fromDate;

    private LocalDate toDate;
}

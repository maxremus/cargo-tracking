package org.example.cargotracking.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.cargotracking.entity.Company;
import org.example.cargotracking.entity.IncomingLoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomingLoadRepository extends JpaRepository<IncomingLoad, Long> {

    boolean existsByInvoiceNumberAndSupplierCompanyAndCompany(String invoiceNumber, String supplierCompany, Company company);

    List<IncomingLoad> findAllByCompany(Company company);

    Optional<IncomingLoad> findByIdAndCompany(Long id, Company company);

    boolean existsByInvoiceNumberAndSupplierCompany(@NotBlank(message = "Номерът на фактурата е задължителен") @Size(
            min = 10,
            max = 10,
            message = "Номерът на фактурата трябва да е 10 символа"
    ) String invoiceNumber, @NotBlank(message = "Фирмата е задължителна") @Size(
            min = 2,
            max = 150,
            message = "Фирмата трябва да е между 2 и 150 символа"
    ) String supplierCompany);
}

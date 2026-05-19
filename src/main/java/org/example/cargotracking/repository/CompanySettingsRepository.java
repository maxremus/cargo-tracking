package org.example.cargotracking.repository;

import org.example.cargotracking.entity.Company;
import org.example.cargotracking.entity.CompanySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanySettingsRepository extends JpaRepository<CompanySettings, Long> {

    Optional<CompanySettings> findByCompany(Company company );

    Optional<CompanySettings> findByCompany_Id(Long companyId);
}

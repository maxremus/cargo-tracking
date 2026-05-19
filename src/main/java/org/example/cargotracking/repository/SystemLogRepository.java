package org.example.cargotracking.repository;

import org.example.cargotracking.entity.Company;
import org.example.cargotracking.entity.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {


    List<SystemLog> findAllByCompanyOrderByCreatedAtDesc(Company company);

    Page<SystemLog> findAllByCompany(Company company, Pageable pageable);
}

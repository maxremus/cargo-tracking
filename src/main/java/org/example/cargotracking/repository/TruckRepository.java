package org.example.cargotracking.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.cargotracking.entity.Company;
import org.example.cargotracking.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {

    List<Truck> findAllByCompany(Company company);

    Optional<Truck> findByIdAndCompany(Long id, Company company);

    boolean existsByTruckNumberAndCompany(String truckNumber, Company company);

    Optional<Object> findByTruckNumberAndCompany(@NotBlank(message = "Номерът на камиона е задължителен") @Size(min = 3, max = 20, message = "Номерът трябва да е между 3 и 20 символа") String truckNumber, Company currentCompany);
}

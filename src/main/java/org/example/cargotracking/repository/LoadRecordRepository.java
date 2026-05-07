package org.example.cargotracking.repository;

import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoadRecordRepository extends JpaRepository<LoadRecord, Long> {

    List<LoadRecord> findByStatus(LoadStatus status);

    List<LoadRecord> findByProductNameContaining(String keyword);

    List<LoadRecord> findByTruck_TruckNumber(String truckNumber);
}

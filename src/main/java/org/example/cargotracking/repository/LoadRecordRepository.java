package org.example.cargotracking.repository;

import org.example.cargotracking.dto.DailyLoadStatsDTO;
import org.example.cargotracking.dto.LoaderStatsDTO;
import org.example.cargotracking.dto.TruckStatsDTO;
import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.entity.LoadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoadRecordRepository extends JpaRepository<LoadRecord, Long>,
        JpaSpecificationExecutor<LoadRecord> {

    List<LoadRecord> findByStatus(LoadStatus status);

    List<LoadRecord> findByProductNameContaining(String keyword);

    List<LoadRecord> findByTruck_TruckNumber(String truckNumber);

    @Query("""
    SELECT
    l.loadedBy as loader,
    COUNT(l.id) as totalLoads
    FROM LoadRecord l
    GROUP BY l.loadedBy
    ORDER BY COUNT(l.id) DESC
    """)
    List<LoaderStatsDTO> getLoaderStats();


    @Query("""
    SELECT
    l.truck.truckNumber as truck,
    COUNT(l.id) as totalLoads
    FROM LoadRecord l
    GROUP BY l.truck.truckNumber
    ORDER BY COUNT(l.id) DESC
    """)
    List<TruckStatsDTO> getTruckStats();


    @Query("""
    SELECT
    FUNCTION('DATE', l.loadingDate) as date,
    COUNT(l.id) as totalLoads
    FROM LoadRecord l
    GROUP BY FUNCTION('DATE', l.loadingDate)
    ORDER BY FUNCTION('DATE', l.loadingDate)
    """)
    List<DailyLoadStatsDTO> getDailyStats();
}

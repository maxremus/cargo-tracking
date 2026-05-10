package org.example.cargotracking.service;

import org.example.cargotracking.dto.DailyLoadStatsDTO;
import org.example.cargotracking.dto.LoaderStatsDTO;
import org.example.cargotracking.dto.TruckStatsDTO;

import java.util.List;

public interface ReportService {

    List<LoaderStatsDTO> getLoaderStats();

    List<TruckStatsDTO> getTruckStats();

    List<DailyLoadStatsDTO> getDailyStats();
}

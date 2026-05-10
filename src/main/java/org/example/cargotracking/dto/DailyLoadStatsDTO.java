package org.example.cargotracking.dto;

import lombok.Data;


public interface DailyLoadStatsDTO {

    String getDate();

    Long getTotalLoads();
}

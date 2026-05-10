package org.example.cargotracking.dto;

import lombok.Data;
import org.example.cargotracking.entity.LoadStatus;

@Data
public class LoadSearchDTO {

    private String productName;

    private String truckNumber;

    private String loadedBy;

    private LoadStatus status;
}

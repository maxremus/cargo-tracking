package org.example.cargotracking.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.cargotracking.dto.LoadSearchDTO;

public interface ExcelExportService {

    void exportLoadsToExcel(LoadSearchDTO search, HttpServletResponse response ) throws Exception;
}

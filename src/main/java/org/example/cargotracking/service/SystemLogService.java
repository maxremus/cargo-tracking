package org.example.cargotracking.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.cargotracking.entity.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SystemLogService {

    void log(String username, String action, String details, String ip);

    List<SystemLog> findAll();

    Page<SystemLog> findAll(Pageable pageable);

    void exportLogs(HttpServletResponse response) throws Exception;

}

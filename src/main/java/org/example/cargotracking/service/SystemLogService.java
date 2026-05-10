package org.example.cargotracking.service;

import org.example.cargotracking.entity.SystemLog;


import java.util.List;

public interface SystemLogService {

    void log(
            String username,
            String action,
            String details,
            String ip
    );


    List<SystemLog> findAll();

}

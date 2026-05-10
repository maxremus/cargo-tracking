package org.example.cargotracking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.SystemLog;
import org.example.cargotracking.repository.SystemLogRepository;
import org.example.cargotracking.service.SystemLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemLogServiceImpl implements SystemLogService {

    private final SystemLogRepository systemLogRepository;

    public void log(

            String username,

            String action,

            String details,

            String ipAddress

    ) {

        SystemLog log =
                SystemLog.builder()

                        .username(username)

                        .action(action)

                        .details(details)

                        .ipAddress(ipAddress)

                        .createdAt(LocalDateTime.now())

                        .build();

        systemLogRepository.save(log);
    }

    public List<SystemLog> findAll() {

        return systemLogRepository
                .findAll()
                .stream()
                .sorted((a, b) ->
                        b.getCreatedAt()
                                .compareTo(a.getCreatedAt()))
                .toList();
    }
}

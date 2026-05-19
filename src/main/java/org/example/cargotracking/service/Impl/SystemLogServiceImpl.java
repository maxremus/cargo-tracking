package org.example.cargotracking.service.Impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.cargotracking.entity.SystemLog;
import org.example.cargotracking.repository.SystemLogRepository;
import org.example.cargotracking.service.SecurityService;
import org.example.cargotracking.service.SystemLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemLogServiceImpl
        implements SystemLogService {

    private final SystemLogRepository systemLogRepository;
    private final SecurityService securityService;

    @Override
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

                        .company(
                                securityService.getCurrentCompany()
                        )

                        .build();

        systemLogRepository.save(log);
    }

    @Override
    public List<SystemLog> findAll() {

        return systemLogRepository
                .findAllByCompanyOrderByCreatedAtDesc(
                        securityService.getCurrentCompany()
                );
    }

    @Override
    public Page<SystemLog> findAll(
            Pageable pageable
    ) {

        return systemLogRepository
                .findAllByCompany(
                        securityService.getCurrentCompany(),
                        pageable
                );
    }

    @Override
    public void exportLogs(
            HttpServletResponse response
    ) throws Exception {

        response.setContentType(
                "text/csv"
        );

        response.setHeader(

                "Content-Disposition",

                "attachment; filename=system-logs.csv"
        );

        PrintWriter writer =
                response.getWriter();

        writer.println(
                "ID,User,Action,Details,IP Address,Created At"
        );

        List<SystemLog> logs =
                systemLogRepository
                        .findAllByCompanyOrderByCreatedAtDesc(
                                securityService.getCurrentCompany()
                        );

        for (SystemLog log : logs) {

            writer.println(

                    log.getId() + "," +

                            log.getUsername() + "," +

                            log.getAction() + "," +

                            log.getDetails() + "," +

                            log.getIpAddress() + "," +

                            log.getCreatedAt()
            );
        }

        writer.flush();
        writer.close();
    }
}

package org.example.cargotracking.service.Impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.cargotracking.dto.LoadSearchDTO;
import org.example.cargotracking.entity.LoadRecord;
import org.example.cargotracking.service.ExcelExportService;
import org.example.cargotracking.service.LoadRecordService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportServiceImpl
        implements ExcelExportService {

    private final LoadRecordService
            loadRecordService;

    @Override
    public void exportLoadsToExcel(

            LoadSearchDTO search,

            HttpServletResponse response

    ) throws Exception {

        List<LoadRecord> loads =

                loadRecordService.searchLoads(

                        search,

                        PageRequest.of(0, 10000)

                ).getContent();

        Workbook workbook =
                new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet(
                        "Loads"
                );

        // HEADER STYLE

        CellStyle headerStyle =
                workbook.createCellStyle();

        Font headerFont =
                workbook.createFont();

        headerFont.setBold(true);

        headerStyle.setFont(
                headerFont
        );

        // HEADER

        Row header =
                sheet.createRow(0);

        String[] columns = {

                "ID",
                "Продукт",
                "Количество",
                "Камион",
                "Натоварил",
                "Статус",
                "Дата"

        };

        for (int i = 0;
             i < columns.length;
             i++) {

            Cell cell =
                    header.createCell(i);

            cell.setCellValue(
                    columns[i]
            );

            cell.setCellStyle(
                    headerStyle
            );
        }

        int rowNum = 1;

        DateTimeFormatter formatter =

                DateTimeFormatter.ofPattern(
                        "dd.MM.yyyy HH:mm"
                );

        // DATA

        for (LoadRecord load : loads) {

            Row row =
                    sheet.createRow(
                            rowNum++
                    );

            row.createCell(0)

                    .setCellValue(
                            load.getId()
                                    != null
                                    ?
                                    load.getId()
                                    :
                                    0
                    );

            row.createCell(1)

                    .setCellValue(
                            load.getProductName()
                                    != null
                                    ?
                                    load.getProductName()
                                    :
                                    "-"
                    );

            row.createCell(2)

                    .setCellValue(
                            load.getQuantity()
                                    != null
                                    ?
                                    load.getQuantity()
                                    :
                                    0
                    );

            row.createCell(3)

                    .setCellValue(

                            load.getTruck() != null

                                    ?

                                    load.getTruck()
                                            .getTruckNumber()

                                    :

                                    "-"
                    );

            row.createCell(4)

                    .setCellValue(
                            load.getLoadedBy()
                                    != null
                                    ?
                                    load.getLoadedBy()
                                    :
                                    "-"
                    );

            row.createCell(5)

                    .setCellValue(

                            load.getStatus() != null

                                    ?

                                    load.getStatus()
                                            .getDisplayName()

                                    :

                                    "-"
                    );

            row.createCell(6)

                    .setCellValue(

                            load.getLoadingDate()
                                    != null

                                    ?

                                    load.getLoadingDate()
                                            .format(formatter)

                                    :

                                    "-"
                    );
        }

        // AUTO SIZE

        for (int i = 0;
             i < columns.length;
             i++) {

            sheet.autoSizeColumn(i);
        }

        // GENERATED INFO

        Row infoRow =
                sheet.createRow(
                        rowNum + 2
                );

        infoRow.createCell(0)

                .setCellValue(

                        "Generated: "

                                +

                                LocalDateTime.now()
                );

        // RESPONSE

        response.setContentType(

                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        response.setHeader(

                "Content-Disposition",

                "attachment; filename=loads.xlsx"
        );

        OutputStream outputStream =
                response.getOutputStream();

        workbook.write(outputStream);

        workbook.close();

        outputStream.close();
    }
}

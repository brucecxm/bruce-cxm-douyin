package com.bruce.service;import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelService {

    /**
     * 创建一个简单的Excel文件并保存到指定路径
     * @throws IOException IO异常
     */
    public void createExcel() throws IOException {
        // 创建一个.xlsx格式的工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建一个名为Sheet1的工作表
        Sheet sheet = workbook.createSheet("Sheet1");

        // 创建第一行
        Row row = sheet.createRow(0);

        // 创建第一个单元格，并写入数据
        Cell cell = row.createCell(0);
        cell.setCellValue("Hello, Spring Boot and Poi!"); // 向单元格写入数据

        // 保存文件
        String filePath = "output.xlsx";
        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close(); // 关闭工作簿
    }
}

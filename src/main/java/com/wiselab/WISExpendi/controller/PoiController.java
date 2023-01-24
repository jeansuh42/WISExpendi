package com.wiselab.WISExpendi.controller;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import com.wiselab.WISExpendi.DTO.ExpenditureCell;
import com.wiselab.WISExpendi.DTO.ExpenditureSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PoiController {

	@GetMapping(value = "/")
	public String getExcelTest() throws Exception{
		return "home";
	}

	@PostMapping(value = "/mail")
	public ResponseEntity<InputStreamResource> downloadExcel(HttpServletResponse response, @RequestBody ExpenditureSheet sheetData) throws IOException {

		String mailAdd = sheetData.getMailAdd();

		String paymentBasis = sheetData.getPaymentBasis();
		String name = sheetData.getName();
		String inputDate = sheetData.getInputDate();

		LocalDate date = LocalDate.parse(inputDate);
		String lastDate = date.withDayOfMonth(date.lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();

		try (Workbook workbook = new SXSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("지출경비");
			sheet.setDisplayGridlines(false);

			int rowNum = 0;
			int sum = 0;

			List<Map> excelList = new ArrayList<Map>();

//			Map<String, Object> headerMap = new HashMap<String, Object>();
//			headerMap.put("경비기준:", paymentBasis);
//			headerMap.put("지급요청일:", lastDate);
//			headerMap.put("지급요청인:", name);
//			excelList.add(headerMap);

			excelList.add(new HashMap<>() {{ put("title", "경비기준"); put("value", paymentBasis); }});
			excelList.add(new HashMap<>() {{ put("title", "지급요청일"); put("value", lastDate); }});
			excelList.add(new HashMap<>() {{ put("title", "지급요청인"); put("value", name); }});

			for(int i=0; i<=2; i++){

				String title = (String) excelList.get(i).get("title") + ":";
				String val = (String) excelList.get(i).get("value");

				Row row = sheet.createRow(rowNum++);
				row.createCell(6).setCellValue(title);
				row.createCell(7).setCellValue(val);
			}

			CellStyle headStyle = workbook.createCellStyle();
			headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
			headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headStyle.setAlignment(HorizontalAlignment.CENTER);

			Font font = workbook.createFont();
			font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
			font.setFontHeightInPoints((short) 13);
			headStyle.setFont(font);

			Row headerRow = sheet.createRow(rowNum++);
			headerRow.createCell(0).setCellValue("일자");
			headerRow.createCell(1).setCellValue("적요");
			headerRow.createCell(2).setCellValue("지불처");
			headerRow.createCell(3).setCellValue("예산액");
			headerRow.createCell(4).setCellValue("지불액");
			headerRow.createCell(5).setCellValue("누계");
			headerRow.createCell(6).setCellValue("프로젝트");
			headerRow.createCell(7).setCellValue("비고");

			for(int i=0; i<=7; i++){
				headerRow.getCell(i).setCellStyle(headStyle);
			}

			for(int i=0; i < sheetData.getCellList().toArray().length; i++){
				Row row = sheet.createRow(rowNum++);
				sum += sheetData.getCellList().get(i).getPaymentAmt();

				row.createCell(0).setCellValue(sheetData.getCellList().get(i).getDate());
				row.createCell(1).setCellValue(sheetData.getCellList().get(i).getAbs());
				row.createCell(2).setCellValue(sheetData.getCellList().get(i).getExpendDepartment());
				row.createCell(3).setCellValue(sheetData.getCellList().get(i).getBudgetAmt());
				row.createCell(4).setCellValue(sheetData.getCellList().get(i).getPaymentAmt());
				row.createCell(5).setCellValue(sum);
				row.createCell(6).setCellValue(sheetData.getCellList().get(i).getProjectNm());
				row.createCell(7).setCellValue(sheetData.getCellList().get(i).getEtc());
			}

			sheet.setColumnWidth(0, 3000);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 8000);
			sheet.setColumnWidth(4, 8000);
			sheet.setColumnWidth(5, 8000);
			sheet.setColumnWidth(6, 3000);
			sheet.setColumnWidth(7, 3000);

			File tmpFile = File.createTempFile("TMP~", ".xlsx");
			try (OutputStream fos = new FileOutputStream(tmpFile);) {
				workbook.write(fos);
			}
			InputStream res = new FileInputStream(tmpFile) {
				@Override
				public void close() throws IOException {
					super.close();
					if (tmpFile.delete()) {
						System.out.println("임시파일 삭제 완료");
					}
				}
			};

			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment;filename=지출경비.xlsx") //
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(new InputStreamResource(res));

		}
	}
}

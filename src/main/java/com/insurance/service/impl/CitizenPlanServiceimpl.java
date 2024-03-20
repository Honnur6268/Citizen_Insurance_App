package com.insurance.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.insurance.binding.SearchCriteria;
import com.insurance.entity.CitizenPlan;
import com.insurance.repo.CitizenPlanRepo;
import com.insurance.service.CitizenPlanService;
import com.insurance.utils.EmailUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CitizenPlanServiceimpl implements CitizenPlanService {

	@Autowired
	private CitizenPlanRepo planRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public List<String> getPlanNames() {

		return planRepo.getUniquePlanName();
	}

	@Override
	public List<String> getPlanStatus() {

		return planRepo.getUniquePlanStatus();
	}

	@Override
	public List<CitizenPlan> searchCitizen(SearchCriteria criteria) {

		CitizenPlan citizen = new CitizenPlan();

		if (StringUtils.isNotBlank(criteria.getPlanName())) {
			citizen.setPlanName(criteria.getPlanName());
		}
		if (StringUtils.isNotBlank(criteria.getPlanStatus())) {
			citizen.setPlanStatus(criteria.getPlanStatus());
		}
		if (StringUtils.isNotBlank(criteria.getGender())) {
			citizen.setGender(criteria.getGender());
		}
		if (criteria.getPlanStartDate() != null) {
			citizen.setPlanStartDate(criteria.getPlanStartDate());
		}
		if (criteria.getPlanEndDate() != null) {
			citizen.setPlanEndDate(criteria.getPlanEndDate());
		}

		Example<CitizenPlan> filter = Example.of(citizen);

		return planRepo.findAll(filter);
	}

	@Override
	public void generateExcel(HttpServletResponse response) throws Exception {

		List<CitizenPlan> citizensInfo = planRepo.findAll();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Citizen Plan Info");
		HSSFRow row = sheet.createRow(0);

		row.createCell(0).setCellValue("Name");
		row.createCell(1).setCellValue("Email");
		row.createCell(2).setCellValue("Mobile Number");
		row.createCell(3).setCellValue("Gender");
		row.createCell(4).setCellValue("SSN");
		row.createCell(5).setCellValue("Plan Name");
		row.createCell(6).setCellValue("Plan Status");

		int cellRowDataIndex = 1;

		for (CitizenPlan citizen : citizensInfo) {
			HSSFRow cellRowData = sheet.createRow(cellRowDataIndex);
			cellRowData.createCell(0).setCellValue(citizen.getName());
			cellRowData.createCell(1).setCellValue(citizen.getEmail());
			cellRowData.createCell(2).setCellValue(citizen.getPhno());
			cellRowData.createCell(3).setCellValue(citizen.getGender());
			cellRowData.createCell(4).setCellValue(citizen.getSsn());
			cellRowData.createCell(5).setCellValue(citizen.getPlanName());
			cellRowData.createCell(6).setCellValue(citizen.getPlanStatus());

			cellRowDataIndex++;
		}

		// To send File As Attachment
		File f = new File("report.xls");
		FileOutputStream fos = new FileOutputStream(f);
		workbook.write(fos);
		emailUtils.sendEmail(f);

		// To download file from browser
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

	@Override
	public void generatePdf(HttpServletResponse response) throws Exception {
		List<CitizenPlan> citizensInfo = planRepo.findAll();

		Document browserDocument = new Document(PageSize.A4);
		ServletOutputStream outputStream = response.getOutputStream();
		PdfWriter.getInstance(browserDocument, outputStream);
		browserDocument.open();

		Document emailDocument = new Document(PageSize.A4);
		File f = new File("report.pdf");
		FileOutputStream fos = new FileOutputStream(f);
		PdfWriter.getInstance(emailDocument, fos);
		emailDocument.open();

		Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD);

		Paragraph paragraph = new Paragraph("Citizen Insurance Report", fontTitle);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		browserDocument.add(paragraph);
		emailDocument.add(paragraph);

		PdfPTable table = new PdfPTable(7);

		table.setWidthPercentage(100);
		table.setWidths(new int[] { 3, 6, 4, 3, 3, 3, 4 });
		table.setSpacingBefore(5);

		PdfPCell cell = new PdfPCell();

		cell.setBackgroundColor(CMYKColor.GRAY);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(CMYKColor.WHITE);

		cell.setPhrase(new Phrase("Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Email", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Mobile No", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Gender", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("SSN", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Plan Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Plan Status", font));
		table.addCell(cell);

		for (CitizenPlan citizen : citizensInfo) {

			table.addCell(citizen.getName());

			table.addCell(citizen.getEmail());
			table.addCell(String.valueOf(citizen.getPhno()));
			table.addCell(citizen.getGender());
			table.addCell(String.valueOf(citizen.getSsn()));
			table.addCell(citizen.getPlanName());
			table.addCell(citizen.getPlanStatus());
		}
		// Adding the created table to the document
		browserDocument.add(table);
		emailDocument.add(table);

		// Closing the document
		browserDocument.close();
		outputStream.close();

		emailDocument.close();
		fos.close();

		emailUtils.sendEmail(f);

	}
}

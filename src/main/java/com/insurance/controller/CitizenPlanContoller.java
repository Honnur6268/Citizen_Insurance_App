package com.insurance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.insurance.binding.SearchCriteria;
import com.insurance.entity.CitizenPlan;
import com.insurance.service.CitizenPlanService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CitizenPlanContoller {

	@Autowired
	private CitizenPlanService service;

	@GetMapping("/")
	public String index(Model model) {
		planAndStatusInit(model);

		model.addAttribute("search", new SearchCriteria());

		return "index";
	}

	private void planAndStatusInit(Model model) {
		List<String> planNames = service.getPlanNames();
		List<String> planStatus = service.getPlanStatus();

		model.addAttribute("planNames", planNames);
		model.addAttribute("planStatus", planStatus);
	}

	@PostMapping("/data")
	public String handleSearchCriteria(@ModelAttribute("search") SearchCriteria criteria, Model model) {
		planAndStatusInit(model);
		System.out.println(criteria);
		List<CitizenPlan> citizensInfo = service.searchCitizen(criteria);
		model.addAttribute("citizensInfo", citizensInfo);
//		model.addAttribute("search", new SearchCriteria());

		return "index";
	}

	@GetMapping("/excel")
	public void generateExcelReport(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=Citizen_Insurance_Report.xls";

		response.setHeader(headerKey, headerValue);

		service.generateExcel(response);
	}

	@GetMapping("/pdf")
	public void generatePdfReport(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=Citizen_Insurance_Report.pdf";

		response.setHeader(headerKey, headerValue);

		service.generatePdf(response);
	}

}

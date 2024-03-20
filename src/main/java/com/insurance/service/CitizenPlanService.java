package com.insurance.service;

import java.util.List;

import com.insurance.binding.SearchCriteria;
import com.insurance.entity.CitizenPlan;

import jakarta.servlet.http.HttpServletResponse;

public interface CitizenPlanService {

	public List<String> getPlanNames();

	public List<String> getPlanStatus();

	public List<CitizenPlan> searchCitizen(SearchCriteria criteriaO);

	public void generateExcel(HttpServletResponse response) throws Exception;

	public void generatePdf(HttpServletResponse response) throws Exception;

}

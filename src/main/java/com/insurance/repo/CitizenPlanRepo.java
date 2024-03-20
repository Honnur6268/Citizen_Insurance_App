package com.insurance.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.insurance.entity.CitizenPlan;

public interface CitizenPlanRepo extends JpaRepository<CitizenPlan, Serializable> {

	@Query("select distinct(planName) from CitizenPlan")
	public List<String> getUniquePlanName();

	@Query("select distinct(planStatus) from CitizenPlan")
	public List<String> getUniquePlanStatus();

}

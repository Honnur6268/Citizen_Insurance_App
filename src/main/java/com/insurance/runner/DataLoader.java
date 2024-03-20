package com.insurance.runner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.insurance.entity.CitizenPlan;
import com.insurance.repo.CitizenPlanRepo;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private CitizenPlanRepo repo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		repo.deleteAll();

		CitizenPlan cp1 = new CitizenPlan("Honnur", "honnu@gmail.com", 8123426862L, "Male", 487959L, "Cash", "Approved",
				LocalDate.now(), LocalDate.now().plusMonths(6));

		CitizenPlan cp2 = new CitizenPlan("Bhavana", "bhavana@gmail.com", 9123426862L, "Fe-Male", 387959L, "Cash",
				"Denied", null, null);
		CitizenPlan cp3 = new CitizenPlan("Zoya", "zoya.com", 8073379047L, "Fe-Male", 287959L, "Food", "Approved",
				LocalDate.now(), LocalDate.now().plusMonths(6));
		CitizenPlan cp4 = new CitizenPlan("Jhon", "jhon@gmail.com", 9073379047L, "Male", 587959L, "Food", "Approved",
				LocalDate.now(), LocalDate.now().plusMonths(6));
		CitizenPlan cp5 = new CitizenPlan("Rohan", "rohan@gmail.com", 48789889L, "Male", 987959L, "Food", "Denied",
				null, null);
		CitizenPlan cp6 = new CitizenPlan("Smith", "smith@gmail.com", 7123426862L, "Fe-Male", 1087959L, "Medical",
				"Terminated", LocalDate.now(), LocalDate.now().plusMonths(6));

		List<CitizenPlan> records = Arrays.asList(cp1, cp2, cp3, cp4, cp5, cp6);

		repo.saveAll(records);

	}

}

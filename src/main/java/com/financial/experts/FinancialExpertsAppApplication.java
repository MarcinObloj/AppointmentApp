package com.financial.experts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class FinancialExpertsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialExpertsAppApplication.class, args);
	}

}

package com.marublosso.worktrack.worktrack_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.marublosso.worktrack.worktrack_backend")
public class WorktrackBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorktrackBackendApplication.class, args);
	}

}

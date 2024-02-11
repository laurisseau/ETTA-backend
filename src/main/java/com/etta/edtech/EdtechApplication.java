package com.etta.edtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class EdtechApplication {

	public static void main(String[] args) {
		test();
		SpringApplication.run(EdtechApplication.class, args);
	}

	public static void test(){
		// Get all environment variables
		Map<String, String> envVariables = System.getenv();

		// Iterate over the map and print each variable and its value
		for (Map.Entry<String, String> entry : envVariables.entrySet()) {
			String variable = entry.getKey();
			String value = entry.getValue();
			System.out.println(variable + " =loop= " + value);
		}

		System.out.println(System.getenv("DB_URL") + " last env try");
	}
}

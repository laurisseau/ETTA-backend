package com.etta.edtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EdtechApplication {

	public static void main(String[] args) {
		test();
		SpringApplication.run(EdtechApplication.class, args);
	}

	public static void test(){
		System.out.println(System.getenv("spring_datasource_url"));
		System.out.println(System.getenv("spring_datasource_username"));
		System.out.println(System.getenv("spring_datasource_password"));
	}
}

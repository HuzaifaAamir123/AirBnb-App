package com.AirBnb.Final.Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirBnbFinalProjectApplication {

	public static void main(String[] args) {

		SpringApplication.run(AirBnbFinalProjectApplication.class, args);
		System.out.println("AirBnb Final Project");

	}

}

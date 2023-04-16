package com.bigbrain.v1;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


//CommandLineRunner is used to run some code once after the app starts
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
@EnableScheduling
public class BigBrainApplication {
	

	
	public static void main(String[] args) {
		SpringApplication.run(BigBrainApplication.class, args);
	}


}

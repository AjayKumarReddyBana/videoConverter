package com.mizutest.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@ComponentScan(basePackages = "com.mizutest.demo")
public class MizuTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MizuTestApplication.class, args);
	}

}

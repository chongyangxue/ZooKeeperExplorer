package com.zyuc.zkui;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 
 * @author xuechongyang
 * 
 */
@SpringBootApplication 
@EnableScheduling
public class Starter implements CommandLineRunner {

	static Logger logger = Logger.getLogger(Starter.class);

	public static void main(String[] args) throws Exception {
		
		SpringApplication app = new SpringApplication(Starter.class);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}

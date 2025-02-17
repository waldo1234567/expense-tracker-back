package com.example.expense.expense_tracker;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpenseTrackerApplication {

	public static void main(String[] args) {
		System.setProperty("SERVER_ADDRESS", System.getenv("SERVER_ADDRESS"));
		System.setProperty("DB_USERNAME", System.getenv("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", System.getenv("DB_PASSWORD"));
		System.setProperty("DB_NAME" , System.getenv("DB_NAME"));
		System.setProperty("DB_HOST", System.getenv("DB_HOST"));
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

}

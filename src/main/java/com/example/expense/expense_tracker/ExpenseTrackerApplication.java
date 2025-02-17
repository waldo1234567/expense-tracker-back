package com.example.expense.expense_tracker;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpenseTrackerApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().directory("src/main/resources").ignoreIfMissing().load();
		System.setProperty("DB_USERNAME", System.getenv("DB_USERNAME") != null ? System.getenv("DB_USERNAME") : dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : dotenv.get("DB_PASSWORD"));
		System.setProperty("DB_NAME", System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : dotenv.get("DB_NAME"));
		System.setProperty("DB_HOST", System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : dotenv.get("DB_HOST"));
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

}

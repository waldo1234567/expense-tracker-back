package com.example.expense.expense_tracker.config;

import com.example.expense.expense_tracker.expense_entity.Expense;
import com.example.expense.expense_tracker.repos.ExpenseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Configuration
public class ExpenseConfig {
    @Bean
    CommandLineRunner commandLineRunner(ExpenseRepository repos){
        YearMonth currentMonth = YearMonth.now();
        int year = currentMonth.getYear();
        int month = currentMonth.getMonthValue();

        return args -> {
            List<Expense> expenses = List.of(
                    new Expense("Groceries", 2100.00, "Food", LocalDateTime.of(2023, 1, 5, 10, 0)),
                    new Expense("Gym Membership", 800.00, "Health", LocalDateTime.of(2023, 2, 1, 9, 0)),
                    new Expense("Car Repair", 4500.00, "Transport", LocalDateTime.of(2023, 3, 10, 15, 30)),
                    new Expense("Weekend Trip", 6000.00, "Entertainment", LocalDateTime.of(2023, 4, 15, 12, 0)),
                    new Expense("Dining Out", 1800.00, "Food", LocalDateTime.of(2023, 5, 20, 20, 0)),
                    new Expense("Online Shopping", 3200.00, "Shopping", LocalDateTime.of(2023, 6, 8, 17, 45)),
                    new Expense("Gas", 1500.00, "Transport", LocalDateTime.of(2023, 7, 3, 14, 0)),
                    new Expense("New Laptop", 12000.00, "Electronics", LocalDateTime.of(2023, 8, 10, 11, 0)),
                    new Expense("Netflix Subscription", 500.00, "Entertainment", LocalDateTime.of(2023, 9, 1, 12, 0)),
                    new Expense("Medical Checkup", 3800.00, "Health", LocalDateTime.of(2023, 10, 5, 9, 0)),
                    new Expense("Groceries", 2500.00, "Food", LocalDateTime.of(2023, 11, 7, 10, 0)),
                    new Expense("Christmas Gifts", 7500.00, "Shopping", LocalDateTime.of(2023, 12, 20, 16, 0)),

                    // 2024 Data
                    new Expense("Dining Out", 2200.00, "Food", LocalDateTime.of(2024, 1, 18, 20, 0)),
                    new Expense("Gas", 1600.00, "Transport", LocalDateTime.of(2024, 2, 10, 14, 0)),
                    new Expense("New Phone", 10000.00, "Electronics", LocalDateTime.of(2024, 3, 5, 10, 30)),
                    new Expense("Spotify Subscription", 600.00, "Entertainment", LocalDateTime.of(2024, 4, 2, 12, 0)),
                    new Expense("Flight Tickets", 8500.00, "Travel", LocalDateTime.of(2024, 5, 12, 9, 0)),
                    new Expense("Dining Out", 2000.00, "Food", LocalDateTime.of(2024, 6, 25, 19, 0)),
                    new Expense("Furniture Purchase", 9500.00, "Home", LocalDateTime.of(2024, 7, 6, 15, 30)),
                    new Expense("Electricity Bill", 3200.00, "Utilities", LocalDateTime.of(2024, 8, 22, 18, 0)),
                    new Expense("Weekend Trip", 6700.00, "Entertainment", LocalDateTime.of(2024, 9, 15, 12, 0)),
                    new Expense("Car Maintenance", 5000.00, "Transport", LocalDateTime.of(2024, 10, 10, 11, 0)),
                    new Expense("Black Friday Shopping", 9800.00, "Shopping", LocalDateTime.of(2024, 11, 29, 16, 0)),
                    new Expense("Holiday Travel", 12500.00, "Travel", LocalDateTime.of(2024, 12, 23, 9, 0)),

                    new Expense("Dining Out", 2200.00, "Food", LocalDateTime.of(year, month, 5, 19, 0)),
                    new Expense("Gas", 1600.00, "Transport", LocalDateTime.of(year, month, 10, 14, 0)),
                    new Expense("Electricity Bill", 3200.00, "Utilities", LocalDateTime.of(year, month, 15, 18, 30)),
                    new Expense("Netflix Subscription", 500.00, "Entertainment", LocalDateTime.of(year, month, 1, 12, 0)),
                    new Expense("Groceries", 2500.00, "Food", LocalDateTime.of(year, month, 7, 10, 0)),
                    new Expense("Car Maintenance", 5000.00, "Transport", LocalDateTime.of(year, month, 20, 11, 45)),
                    new Expense("Weekend Trip", 6700.00, "Entertainment", LocalDateTime.of(year, month, 24, 12, 0))
            );

            repos.saveAll(expenses);
            repos.flush();
        };
    }
}

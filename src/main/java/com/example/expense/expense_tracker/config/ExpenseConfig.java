package com.example.expense.expense_tracker.config;

import com.example.expense.expense_tracker.expense_entity.Expense;
import com.example.expense.expense_tracker.expense_entity.Income;
import com.example.expense.expense_tracker.expense_entity.SavingGoal;
import com.example.expense.expense_tracker.repos.ExpenseRepository;
import com.example.expense.expense_tracker.repos.IncomeRepository;
import com.example.expense.expense_tracker.repos.SavingRepository;
import org.apache.commons.math3.analysis.function.Exp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Configuration
public class ExpenseConfig {
    @Bean
    CommandLineRunner commandLineRunner(ExpenseRepository repos, IncomeRepository incomeRepos, SavingRepository savingRepository){


        return args -> {
            YearMonth currentYm = YearMonth.now();
            int cy = currentYm.getYear(), cm = currentYm.getMonthValue();

            List<Expense> expenses = new ArrayList<>();
            Random rnd = new Random(42);

            // 1.A) 2023—every month: rent, utilities, groceries, transport
            for (int month = 1; month <= 12; month++) {
                // Rent: 1200/month on the 1st of each month at 10:00
                expenses.add(new Expense(
                        "Monthly Rent",
                        1200.00,
                        "Housing",
                        LocalDateTime.of(2023, month, 1, 10, 0)
                ));

                // Utilities: varying between 150–250 on the 5th
                double utilAmt = 150 + rnd.nextInt(101); // 150–250
                expenses.add(new Expense(
                        "Utilities (electric/gas)",
                        utilAmt,
                        "Utilities",
                        LocalDateTime.of(2023, month, 5, 18, 30)
                ));

                // Groceries: 300–500 randomly on day between 10–15
                double groceryAmt = 300 + rnd.nextInt(201); // 300–500
                int grocDay = 10 + rnd.nextInt(6);
                expenses.add(new Expense(
                        "Groceries",
                        groceryAmt,
                        "Food",
                        LocalDateTime.of(2023, month, grocDay, 12, 0)
                ));

                // Transport (gas/public transit): 100–200 on the 20th
                double transAmt = 100 + rnd.nextInt(101);
                expenses.add(new Expense(
                        "Transport",
                        transAmt,
                        "Transport",
                        LocalDateTime.of(2023, month, 20, 9, 0)
                ));

                // One “entertainment” or “dining out” purchase per quarter
                if (month % 3 == 0) {
                    double dineAmt = 50 + rnd.nextInt(151); // 50–200
                    expenses.add(new Expense(
                            "Dining Out",
                            dineAmt,
                            "Entertainment",
                            LocalDateTime.of(2023, month, 18, 19, 0)
                    ));
                }
            }

            // 1.B) 2024—scale up a little: rent rises to 1250, utilities 175–300, groceries 350–550
            for (int month = 1; month <= 12; month++) {
                // Rent: $1,250 on 1st
                expenses.add(new Expense(
                        "Monthly Rent",
                        1250.00,
                        "Housing",
                        LocalDateTime.of(2024, month, 1, 10, 0)
                ));

                // Utilities: 175–300 on the 5th
                double utilAmt = 175 + rnd.nextInt(126); // 175–300
                expenses.add(new Expense(
                        "Utilities (electric/gas)",
                        utilAmt,
                        "Utilities",
                        LocalDateTime.of(2024, month, 5, 18, 30)
                ));

                // Groceries: 350–550 on day 12–17
                double groceryAmt = 350 + rnd.nextInt(201); // 350–550
                int grocDay = 12 + rnd.nextInt(6);
                expenses.add(new Expense(
                        "Groceries",
                        groceryAmt,
                        "Food",
                        LocalDateTime.of(2024, month, grocDay, 12, 0)
                ));

                // Transport: 120–220 on day 22
                double transAmt = 120 + rnd.nextInt(101);
                expenses.add(new Expense(
                        "Transport",
                        transAmt,
                        "Transport",
                        LocalDateTime.of(2024, month, 22, 9, 0)
                ));

                // Subscription: $15–20 for streaming on the 10th
                double subAmt = 15 + rnd.nextInt(6); // 15–20
                expenses.add(new Expense(
                        "Streaming Subscription",
                        subAmt,
                        "Subscription",
                        LocalDateTime.of(2024, month, 10, 12, 0)
                ));

                // One large “one-off” purchase mid‐year: new furniture or electronics
                if (month == 7) {
                    double furnAmt = 9500.00;
                    expenses.add(new Expense(
                            "Furniture Purchase",
                            furnAmt,
                            "Home",
                            LocalDateTime.of(2024, 7, 6, 15, 30)
                    ));
                }
                if (month == 9) {
                    double tripAmt = 6700.00;
                    expenses.add(new Expense(
                            "Weekend Trip",
                            tripAmt,
                            "Entertainment",
                            LocalDateTime.of(2024, 9, 15, 12, 0)
                    ));
                }
            }

            List<Expense> expenses2025 = List.of(

                    new Expense("Monthly Rent", 1300.00, "Housing", LocalDateTime.of(2025, 1, 1, 10, 0)),
                    new Expense("Utilities (electric/gas)", 220.00, "Utilities", LocalDateTime.of(2025, 1, 5, 18, 30)),
                    new Expense("Groceries", 450.00, "Food", LocalDateTime.of(2025, 1, 12, 12, 0)),
                    new Expense("Transport", 150.00, "Transport", LocalDateTime.of(2025, 1, 20, 9, 0)),

                    new Expense("Monthly Rent", 1300.00, "Housing", LocalDateTime.of(2025, 2, 1, 10, 0)),
                    new Expense("Utilities (electric/gas)", 230.00, "Utilities", LocalDateTime.of(2025, 2, 5, 18, 30)),
                    new Expense("Groceries", 480.00, "Food", LocalDateTime.of(2025, 2, 13, 12, 0)),
                    new Expense("Transport", 160.00, "Transport", LocalDateTime.of(2025, 2, 20, 9, 0)),

                    new Expense("Monthly Rent", 1300.00, "Housing", LocalDateTime.of(2025, 3, 1, 10, 0)),
                    new Expense("Utilities (electric/gas)", 240.00, "Utilities", LocalDateTime.of(2025, 3, 5, 18, 30)),
                    new Expense("Groceries", 500.00, "Food", LocalDateTime.of(2025, 3, 14, 12, 0)),
                    new Expense("Transport", 170.00, "Transport", LocalDateTime.of(2025, 3, 20, 9, 0)),

                    new Expense("Monthly Rent", 1300.00, "Housing", LocalDateTime.of(2025, 4, 1, 10, 0)),
                    new Expense("Utilities (electric/gas)", 260.00, "Utilities", LocalDateTime.of(2025, 4, 5, 18, 30)),
                    new Expense("Groceries", 520.00, "Food", LocalDateTime.of(2025, 4, 12, 12, 0)),
                    new Expense("Transport", 180.00, "Transport", LocalDateTime.of(2025, 4, 20, 9, 0)),
                    // plus an ad‐hoc “Netflix” subscription and “random” weekend spend in April:
                    new Expense("Netflix Subscription", 15.00, "Subscription", LocalDateTime.of(2025, 4, 8, 12, 0)),
                    new Expense("Dining Out", 120.00, "Entertainment", LocalDateTime.of(2025, 4, 15, 19, 0)),

                    new Expense("Monthly Rent", 1300.00, "Housing", LocalDateTime.of(2025, 5, 1, 10, 0)),
                    new Expense("Utilities (electric/gas)", 250.00, "Utilities", LocalDateTime.of(2025, 5, 5, 18, 30)),
                    new Expense("Groceries", 550.00, "Food", LocalDateTime.of(2025, 5, 11, 12, 0)),
                    new Expense("Transport", 190.00, "Transport", LocalDateTime.of(2025, 5, 20, 9, 0)),

                    new Expense("Monthly Rent", 1300.00, "Housing", LocalDateTime.of(2025, 6, 1, 10, 0)),
                    new Expense("Utilities (electric/gas)", 260.00, "Utilities", LocalDateTime.of(2025, 6, 5, 18, 30)),
                    new Expense("Groceries", 580.00, "Food", LocalDateTime.of(2025, 6, 12, 12, 0)),
                    new Expense("Transport", 200.00, "Transport", LocalDateTime.of(2025, 6, 20, 9, 0))
            );

            expenses.addAll(expenses2025);

            int batchSize = 10;
            for(int i =0 ; i < expenses.size(); i += batchSize){
                int end = Math.min(i + batchSize, expenses.size());
                List<Expense> batch = expenses.subList(i, end);
                repos.saveAll(batch);
                System.out.println("Inserted Expense batch: indexes " + i + "–" + (end - 1));
            }
            repos.flush();

            List<SavingGoal> financialGoals = new ArrayList<>();

            financialGoals.add(new SavingGoal(
                    "Emergency Target fund",
                    new BigDecimal("10000.00"),
                    BigDecimal.ZERO
            ));

            financialGoals.add(new SavingGoal(
                    "Long-Term Investment Goal",
                    new BigDecimal("50000.00"),
                    BigDecimal.ZERO
            ));

            financialGoals.add(new SavingGoal(
                    "Debt-Free Living",
                    new BigDecimal("0.00"),
                    BigDecimal.ZERO
            ));

            savingRepository.saveAll(financialGoals);

        };
    }
}

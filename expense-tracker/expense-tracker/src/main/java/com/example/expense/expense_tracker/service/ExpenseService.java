package com.example.expense.expense_tracker.service;

import com.example.expense.expense_tracker.expense_entity.DailySummaryDTO;
import com.example.expense.expense_tracker.expense_entity.Expense;
import com.example.expense.expense_tracker.expense_entity.MonthlyDTO;
import com.example.expense.expense_tracker.repos.ExpenseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAllExpense(){
       return expenseRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    public Expense saveExpense(Expense expense){
         return expenseRepository.save(expense);
    }

    public void deleteExpense(Long id){
        expenseRepository.deleteById(id);
    }

    public Map<String, Double> getExpenseSummary(){
        List<Object[]> result = expenseRepository.getExpenseTotalFromCategory();
        return result.stream().collect(Collectors.toMap(
                arr -> (String) arr[0],
                arr -> (arr[1] != null) ? (Double) arr[1] : 0.0
        ));
    }

    public DailySummaryDTO getCurrentMonth(){
        List<Expense>expenses = expenseRepository.getCurrentMonthExpenses();
        double thisYear = expenseRepository.getThisYearExpenses();

        double totalExpense = expenses.stream().mapToDouble(Expense :: getAmount).sum();
        int currentDay = LocalDateTime.now().getDayOfMonth();
        double dailyAverage = (currentDay > 0) ? (totalExpense / currentDay) : 0.0;

        return new DailySummaryDTO(thisYear,totalExpense,dailyAverage);
    }

    public List<MonthlyDTO> getTotalExpenseByMonth(){
        List<Object[]> result = expenseRepository.getExpenseByMonth();
        return result.stream()
                .map(r -> new MonthlyDTO(
                        (Integer) r[0],
                        (Integer) r[1],
                        (r[2] != null) ? (Double) r[2] : 0.0
                ))
                .collect(Collectors.toList());
    }

}

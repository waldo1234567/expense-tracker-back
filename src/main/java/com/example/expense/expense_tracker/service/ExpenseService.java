package com.example.expense.expense_tracker.service;

import com.example.expense.expense_tracker.expense_entity.DailySummaryDTO;
import com.example.expense.expense_tracker.expense_entity.Expense;
import com.example.expense.expense_tracker.expense_entity.MonthlyDTO;
import com.example.expense.expense_tracker.repos.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Optional<Expense> getExpenseById(Long id) { return expenseRepository.findById(id);}

    public Optional<List<Expense>> getExpenseByDate(LocalDateTime start, LocalDateTime end){
        return expenseRepository.findByDateBetween(start,end);

    }

    public Map<String, Double> getExpenseSummary(int page , int size){
        Pageable pageable = PageRequest.of(page , size);
        List<Object[]> result = expenseRepository.getExpenseTotalFromCategory(pageable);
        return result.stream().collect(Collectors.toMap(
                arr -> (String) arr[0],
                arr -> (arr[1] != null) ? (Double) arr[1] : 0.0
        ));
    }

    public DailySummaryDTO getCurrentMonth(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Expense>expensesPage = expenseRepository.getCurrentMonthExpenses(pageable);

        List<Expense>expenses = expensesPage.getContent();

        double thisYear = expenseRepository.getThisYearExpenses("2025-01-01", "2025-12-31");

        double totalExpense = expenses.stream().mapToDouble(Expense :: getAmount).sum();
        int currentDay = LocalDateTime.now().getDayOfMonth();
        double dailyAverage = (currentDay > 0) ? (totalExpense / currentDay) : 0.0;

        return new DailySummaryDTO(thisYear,totalExpense,dailyAverage);
    }

    @Transactional(readOnly = true)
    public List<MonthlyDTO> getTotalExpenseByMonth(){
        Stream<Object[]> expenseDataStream = expenseRepository.getExpenseByMonth();

        List<Object[]> expenseData = expenseDataStream.collect(Collectors.toList());

        List<MonthlyDTO> monthlySummary = new ArrayList<>();
        for (Object[] row : expenseData) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            double totalAmount = ((Number) row[2]).doubleValue();
            monthlySummary.add(new MonthlyDTO(year, month, totalAmount));
        }

        return monthlySummary;
    }

    public Map<String, Double> getCategorySummaryForCurrentMonth(int page, int size) {
        LocalDate today        = LocalDate.now();
        LocalDate firstOfMonth = today.withDayOfMonth(1);
        LocalDate lastOfMonth  = today.withDayOfMonth(today.lengthOfMonth());

        // Convert to LocalDateTime: start at 00:00:00 on first day, end at 23:59:59 on last day
        LocalDateTime startOfMonthTime = firstOfMonth.atStartOfDay();
        LocalDateTime endOfMonthTime   = lastOfMonth.atTime(LocalTime.MAX);

        Pageable pageable = PageRequest.of(page, size);
        List<Object[]> rows = expenseRepository.findCategoryTotalsForCurrentMonth(
                startOfMonthTime, endOfMonthTime, pageable
        );

        return rows.stream().collect(Collectors.toMap(
                arr -> (String) arr[0],
                arr -> ((Number) arr[1]).doubleValue()
        ));
    }

    public Page<Expense> searchExpense(
            String searchTerm,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page,size, Sort.by(direction, sortBy));

        return expenseRepository.findByDescriptionContainingIgnoreCase(searchTerm, pageable);
    }

}

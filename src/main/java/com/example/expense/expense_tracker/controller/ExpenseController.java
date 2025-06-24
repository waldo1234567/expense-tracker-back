package com.example.expense.expense_tracker.controller;

import com.example.expense.expense_tracker.expense_entity.DailySummaryDTO;
import com.example.expense.expense_tracker.expense_entity.Expense;
import com.example.expense.expense_tracker.expense_entity.MonthlyDTO;
import com.example.expense.expense_tracker.service.ExpendsPredictionService;
import com.example.expense.expense_tracker.service.ExpenseService;
import org.apache.commons.math3.analysis.function.Exp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(path = "api/expense")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ExpendsPredictionService expendsPredictionService;


    @Autowired
    public ExpenseController(ExpenseService expenseService, ExpendsPredictionService expendsPredictionService){
        this.expenseService = expenseService;
        this.expendsPredictionService = expendsPredictionService;
    }

    @GetMapping
    public List<Expense> getExpense (){
        System.out.println("Fetched expenses: " + expenseService.getAllExpense());
        return expenseService.getAllExpense();
    }

    @GetMapping("/summary-from-category")
    public Map<String,Double> getSummaryFromCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return expenseService.getExpenseSummary(page,size);
    }

    @GetMapping("/get-by-date")
    public Optional<List<Expense>> getByDate(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
            ){
        return expenseService.getExpenseByDate(start,end);
    }

    @GetMapping("/summary-by-month")
    @Transactional(readOnly = true)
    public List<MonthlyDTO> getSummaryByMonth(){
        return expenseService.getTotalExpenseByMonth();
    }

    @GetMapping("/current-month")
    public DailySummaryDTO getCurrentMonthExp(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return expenseService.getCurrentMonth(page, size);
    }

    @GetMapping("/predict")
    public Map<String,Object> getExpensePrediction(){
        double predictedAmount = expendsPredictionService.predictNextMonthExpense();
        return Map.of(
                "predicted_expense", predictedAmount
        );
    }

    @PostMapping
    public Expense addNewExpense(@RequestBody Expense expense){
        Expense savedExpense =  expenseService.saveExpense(expense);
        return savedExpense;
    }

    @DeleteMapping(path = "/{expenseId}")
    public void deleteExpense(@PathVariable("expenseId") Long expenseId){
        expenseService.deleteExpense(expenseId);
    }


}

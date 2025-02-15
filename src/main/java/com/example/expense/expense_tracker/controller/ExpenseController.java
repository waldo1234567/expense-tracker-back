package com.example.expense.expense_tracker.controller;

import com.example.expense.expense_tracker.expense_entity.DailySummaryDTO;
import com.example.expense.expense_tracker.expense_entity.Expense;
import com.example.expense.expense_tracker.expense_entity.MonthlyDTO;
import com.example.expense.expense_tracker.service.ExpendsPredictionService;
import com.example.expense.expense_tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public Map<String,Double> getSummaryFromCategory(){
        return expenseService.getExpenseSummary();
    }

    @GetMapping("/summary-by-month")
    public List<MonthlyDTO> getSummaryByMonth(){
        return expenseService.getTotalExpenseByMonth();
    }

    @GetMapping("/current-month")
    public DailySummaryDTO getCurrentMonthExp(){
        return expenseService.getCurrentMonth();
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

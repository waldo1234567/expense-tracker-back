package com.example.expense.expense_tracker.controller;

import com.example.expense.expense_tracker.expense_entity.Income;
import com.example.expense.expense_tracker.expense_entity.MonthlyDTO;
import com.example.expense.expense_tracker.expense_entity.YearlyIncomeDTO;
import com.example.expense.expense_tracker.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping(path = "api/income")
public class IncomeController {

    private  final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    public List<MonthlyDTO> getIncomeMonthly(
            @RequestParam String start,
            @RequestParam String end
            ){
        return incomeService.findByDate(start ,end);
    }

    @GetMapping("/get-all")
    public List<MonthlyDTO> getAllIncome(){
        return incomeService.fetchAll();
    }

    @PostMapping
    public Income uploadIncome(
            @RequestBody Income income
    ){
        return  incomeService.saveIncome(income);
    }

    @DeleteMapping
    public void deleteIncome(
            @RequestParam Long id
    ){
        incomeService.deleteIncome(id);
    }

    @PutMapping
    public Income updateIncome(
            @RequestParam Long id,
            @RequestBody Income income
    ){
        return incomeService.updateIncome(id, income);
    }

    @PostMapping("/expand-yearly")
    public List<MonthlyDTO> expandYearlyCategories(
            @RequestBody List<YearlyIncomeDTO> yearlyCategories
    ){
        List<MonthlyDTO> monthlySeries = incomeService.expandYearlyCategoryToMonthly(yearlyCategories);
        return monthlySeries;
    }

    @GetMapping("/current-month")
    public MonthlyDTO getCurrentMonthIncome() {
        MonthlyDTO dto = incomeService.getCurrentMonthIncome();
        return dto;
    }
}

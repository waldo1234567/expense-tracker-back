package com.example.expense.expense_tracker.controller;

import com.example.expense.expense_tracker.expense_entity.SavingGoal;
import com.example.expense.expense_tracker.service.SavingGoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping(path = "api/savings")
public class SavingsController {

    private final SavingGoalService savingGoalService;

    public SavingsController(SavingGoalService savingGoalService) {
        this.savingGoalService = savingGoalService;
    }

    @GetMapping
    public ResponseEntity<List<SavingGoal>> getAllFinancialGoals() {
        return ResponseEntity.ok(savingGoalService.getAllSavingGoal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingGoal> getFinancialGoalById(@PathVariable Long id) {
        return savingGoalService.findGoalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SavingGoal> createFinancialGoal(@RequestBody SavingGoal savingGoal) {
        return ResponseEntity.ok(savingGoalService.createSavingGoal(savingGoal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavingGoal> updateFinancialGoal(@PathVariable Long id, @RequestBody SavingGoal financialGoalDetails) {
        try {
            SavingGoal updatedGoal = savingGoalService.updateFinancialGoal(id, financialGoalDetails);
            return ResponseEntity.ok(updatedGoal);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialGoal(@PathVariable Long id) {
        savingGoalService.deleteFinancialGoal(id);
        return ResponseEntity.noContent().build();
    }
}

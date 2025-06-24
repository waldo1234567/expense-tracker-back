package com.example.expense.expense_tracker.service;

import com.example.expense.expense_tracker.expense_entity.Expense;
import com.example.expense.expense_tracker.expense_entity.Income;
import com.example.expense.expense_tracker.expense_entity.SavingGoal;
import com.example.expense.expense_tracker.repos.ExpenseRepository;
import com.example.expense.expense_tracker.repos.IncomeRepository;
import com.example.expense.expense_tracker.repos.SavingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SavingGoalService {
    private final SavingRepository savingRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    public SavingGoalService(
            SavingRepository savingRepository,
            ExpenseRepository expenseRepository,
            IncomeRepository incomeRepository
            ) {
        this.savingRepository = savingRepository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    @Transactional
    public List<SavingGoal> getAllSavingGoal(){
        List<SavingGoal> goals = savingRepository.findAll();
        goals.forEach(this::calculateNetBalance);
        return goals;
    }

    public Optional<SavingGoal> findGoalById(Long id){
        Optional<SavingGoal> goal = savingRepository.findById(id);
        goal.ifPresent(this::calculateNetBalance);
        return goal;
    }

    @Transactional
    public SavingGoal createSavingGoal(SavingGoal savingGoal){
        savingGoal.setCurrentNetBalance(BigDecimal.ZERO);
        return savingRepository.save(savingGoal);
    }

    @Transactional
    public SavingGoal updateFinancialGoal(Long id, SavingGoal savingGoal){
        return savingRepository.findById(id)
                .map(goal -> {
                    goal.setName(savingGoal.getName());
                    goal.setTargetNetBalance(savingGoal.getTargetNetBalance());
                    return savingRepository.save(goal);
                }).orElseThrow(() -> new RuntimeException("Saving Goals not Found"));
    }
    public void deleteFinancialGoal(Long id) {
        savingRepository.deleteById(id);
    }

    private void calculateNetBalance(SavingGoal goal){
        List<Expense> allExpenses;
        allExpenses = expenseRepository.findAll(); // Assuming it returns List<Expense> directly

        BigDecimal totalExpenses = allExpenses.stream()
                .map(expense -> {
                    if (expense.getAmount() == null) {
                        return BigDecimal.ZERO;
                    }
                    return BigDecimal.valueOf(expense.getAmount());
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Income> allIncomes;
        allIncomes = incomeRepository.findAll();

        BigDecimal totalIncomes = allIncomes.stream()
                .map(income -> {
                    if (income.getAmount() == null) {
                        return BigDecimal.ZERO;
                    }
                    return BigDecimal.valueOf(income.getAmount());
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal currentNetBalance = totalIncomes.subtract(totalExpenses);
        goal.setCurrentNetBalance(currentNetBalance);
    }
}

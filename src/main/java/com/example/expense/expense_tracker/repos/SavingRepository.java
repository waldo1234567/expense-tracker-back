package com.example.expense.expense_tracker.repos;

import com.example.expense.expense_tracker.expense_entity.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingRepository extends JpaRepository<SavingGoal, Long> {

}

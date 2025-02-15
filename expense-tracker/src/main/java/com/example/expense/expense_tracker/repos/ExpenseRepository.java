package com.example.expense.expense_tracker.repos;

import com.example.expense.expense_tracker.expense_entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByCategory(String category);

    @Query("SELECT e.category , COALESCE(SUM(e.amount), 0) FROM Expense e GROUP BY e.category")
    List<Object[]> getExpenseTotalFromCategory();

    @Query(
            "SELECT EXTRACT(YEAR FROM e.date), EXTRACT(MONTH FROM e.date), SUM(e.amount) " +
            "FROM Expense e "+
                    "GROUP BY EXTRACT(YEAR FROM e.date),EXTRACT(MONTH FROM e.date) "+
                    "ORDER BY EXTRACT(YEAR FROM e.date) ASC,EXTRACT(MONTH FROM e.date) ASC"
    )
    List<Object[]> getExpenseByMonth();

    @Query(
            "SELECT e FROM Expense e " +
                    "WHERE EXTRACT(YEAR FROM e.date) = EXTRACT(YEAR FROM CURRENT_DATE) " +
                    "AND EXTRACT(MONTH FROM e.date) = EXTRACT(MONTH FROM CURRENT_DATE)"
    )
    List<Expense> getCurrentMonthExpenses();

    @Query(
            "SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
                    "WHERE EXTRACT(YEAR FROM e.date) = EXTRACT(YEAR FROM CURRENT_DATE)"
    )
    Double getThisYearExpenses();
}

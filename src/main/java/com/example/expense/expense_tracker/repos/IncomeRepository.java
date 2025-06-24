package com.example.expense.expense_tracker.repos;

import com.example.expense.expense_tracker.expense_entity.Income;
import com.example.expense.expense_tracker.expense_entity.MonthlyDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    @Query("""
           SELECT new com.example.expense.expense_tracker.expense_entity.MonthlyDTO(
           YEAR(i.date), MONTH(i.date), SUM(i.amount)
           )
            FROM Income i
            WHERE i.date BETWEEN :start AND :end
            GROUP BY YEAR(i.date) , MONTH(i.date)
            ORDER BY YEAR(i.date) , MONTH(i.date)    
    """)
    List<MonthlyDTO> findMonthlySummary(
            @Param("start") java.time.LocalDate start,
            @Param("end") java.time.LocalDate end
    );

    Optional<Income> findByDate(LocalDate date);

    Optional<Income> findTopByOrderByDateDesc();
}

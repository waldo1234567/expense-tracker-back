package com.example.expense.expense_tracker.repos;

import com.example.expense.expense_tracker.expense_entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByCategory(String category);

    @Query("SELECT e.category , COALESCE(SUM(e.amount), 0) FROM Expense e GROUP BY e.category")
    List<Object[]> getExpenseTotalFromCategory(Pageable pageable);

    @Query(
            "SELECT EXTRACT(YEAR FROM e.date), EXTRACT(MONTH FROM e.date), SUM(e.amount) " +
            "FROM Expense e "+
                    "GROUP BY EXTRACT(YEAR FROM e.date),EXTRACT(MONTH FROM e.date) "+
                    "ORDER BY EXTRACT(YEAR FROM e.date) ASC,EXTRACT(MONTH FROM e.date) ASC"
    )
    Stream<Object[]> getExpenseByMonth();

    @Query("""
        SELECT e
           FROM Expense e
          WHERE e.date BETWEEN :start AND :end
          ORDER BY e.date ASC
    """)
    Optional<List<Expense>> findByDateBetween(
            @Param("start") LocalDateTime start,
            @Param("end")   LocalDateTime end
    );

    Page<Expense> findByDescriptionContainingIgnoreCase(String desc, Pageable pageable);

    @Query(
            "SELECT e FROM Expense e " +
                    "WHERE EXTRACT(YEAR FROM e.date) = EXTRACT(YEAR FROM CURRENT_DATE) " +
                    "AND EXTRACT(MONTH FROM e.date) = EXTRACT(MONTH FROM CURRENT_DATE)"
    )
    Page<Expense> getCurrentMonthExpenses(Pageable pageable);

    @Query(
            "SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
                    "WHERE EXTRACT(YEAR FROM e.date) = EXTRACT(YEAR FROM CURRENT_DATE)"
    )
    Double getThisYearExpenses(@Param("startDate") String startDate, @Param("endDate") String endDate);


    @Query("""
      SELECT e.category, SUM(e.amount)
        FROM Expense e
       WHERE e.date BETWEEN :startOfMonthTime AND :endOfMonthTime
       GROUP BY e.category
    """)
    List<Object[]> findCategoryTotalsForCurrentMonth(
            @Param("startOfMonthTime") LocalDateTime startOfMonthTime,
            @Param("endOfMonthTime")   LocalDateTime endOfMonthTime,
            Pageable pageable
    );
}

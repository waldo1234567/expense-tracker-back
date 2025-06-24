package com.example.expense.expense_tracker;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.expense.expense_tracker.expense_entity.DailySummaryDTO;
import com.example.expense.expense_tracker.expense_entity.Expense;
import com.example.expense.expense_tracker.expense_entity.MonthlyDTO;
import com.example.expense.expense_tracker.repos.ExpenseRepository;
import com.example.expense.expense_tracker.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;


import java.time.LocalDateTime;
import java.util.*;


import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense expense;

    @BeforeEach
    void setUp() {
        expense = new Expense();
        expense.setId(1L);
        expense.setCategory("Food");
        expense.setAmount(50.0);
        expense.setDate(LocalDateTime.now());
    }

//    @Test
//    void getAllExpense(){
//        List<Expense> expenses = List.of(expense);
//        when(expenseRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))).thenReturn(expenses);
//
//        Page<Expense> result = expenseService.getAllExpense(0, 12);
//        assertEquals(1, result.size());
//        assertEquals("Food", result.get().getCategory());
//    }

    @Test
    void testSaveExpense(){
        when(expenseRepository.save(expense)).thenReturn(expense);

        Expense savedExpense = expenseService.saveExpense(expense);
        assertNotNull(savedExpense);
        assertEquals(50.0, savedExpense.getAmount());
    }


    @Test
    void testDeleteExpense(){
        doNothing().when(expenseRepository).deleteById(1L);

        assertDoesNotThrow(() -> expenseService.deleteExpense(1L));
        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetExpenseById(){
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        Optional<Expense> foundedExpense = expenseService.getExpenseById(1L);
        assertTrue(foundedExpense.isPresent());
        assertEquals("Food", foundedExpense.get().getCategory());
    }

    @Test
    void testGetExpenseSummary(){
        List<Object[]> mockData = List.of(new Object[]{"Food", 200.00}, new Object[]{"Transport", 100.00});
        Pageable pageable = PageRequest.of(0,10);
        when(expenseRepository.getExpenseTotalFromCategory(pageable)).thenReturn(mockData);

        Map<String,Double> summary = expenseService.getExpenseSummary(0,10);
        assertEquals(2, summary.size());
        assertEquals(200.00, summary.get("Food"));
        assertEquals(100.00, summary.get("Transport"));
    }

    @Test
    void testGetCurrentMonth(){
        List<Expense> expenses = List.of(expense);
        Pageable pageable = PageRequest.of(0,10);
        when(expenseRepository.getCurrentMonthExpenses(pageable)).thenReturn(new PageImpl<>(expenses));
        when(expenseRepository.getThisYearExpenses("2025-01-01", "2025-12-31")).thenReturn(1000.0);

        DailySummaryDTO result = expenseService.getCurrentMonth(0,10);
        assertEquals(1000.0, result.getThisYear());
        assertTrue(result.getTotalExpense() > 0);
    }
    @Test
    void testTotalExpenseByMonth(){
        List<Object[]> mockData = new ArrayList<>();
        mockData.add(new Object[]{2025, 1, 300.0});
        mockData.add(new Object[]{2025, 2, 400.0});
        when(expenseRepository.getExpenseByMonth()).thenReturn(mockData.stream());

        List<MonthlyDTO> result = expenseService.getTotalExpenseByMonth();
        assertEquals(2, result.size());
        assertEquals(300.0, result.get(0).getTotalAmount());
        assertEquals(400.0, result.get(1).getTotalAmount());
    }
}

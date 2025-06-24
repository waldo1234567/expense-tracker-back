package com.example.expense.expense_tracker.service;

import com.example.expense.expense_tracker.repos.ExpenseRepository;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpendsPredictionService {
    private final ExpenseRepository expenseRepository;

    public ExpendsPredictionService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    @Transactional(readOnly = true)
    public double predictNextMonthExpense(){
        List<Object[]> expenseData = expenseRepository.getExpenseByMonth().collect(Collectors.toList());;
        expenseData.sort(Comparator.comparingInt(row -> ((Number) row[1]).intValue()));
        if(expenseData.size() < 4){
            return 0;
        }

        List<Double> months = new ArrayList<>();
        List<Double> expenses = new ArrayList<>();

        for(Object[] row: expenseData){
            double month = ((Number) row[1]).doubleValue();
            double totalExpense = ((Number) row[2]).doubleValue();
            months.add(month);
            expenses.add(totalExpense);
        }

        double[][] features = new double[months.size()][1];
        double[][] labels = new double[expenses.size()][1];

        for(int i = 0; i < months.size(); i++){
            features[i][0] = months.get(i);
            labels[i][0] = expenses.get(i);
        }
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < months.size(); i++) {
            regression.addData(months.get(i), expenses.get(i));
        }
        double nextMonth = months.get(months.size() - 1) + 1;
        double nextTimeIndex = nextMonth + 1;
        double predictedExpense = regression.predict(nextTimeIndex);

        return Math.max(predictedExpense, 0);
    }

}

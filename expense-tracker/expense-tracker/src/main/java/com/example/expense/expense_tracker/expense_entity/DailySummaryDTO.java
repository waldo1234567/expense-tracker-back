package com.example.expense.expense_tracker.expense_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DailySummaryDTO {
    @JsonProperty("thisYear")
    private double thisYear;
    @JsonProperty("totalExpense")
    private double totalExpense;
    @JsonProperty("dailyAverageExpense")
    private double dailyAverageExpense;

    public DailySummaryDTO(double thisYear,double totalExpense, double dailyAverageExpense){
        this.thisYear = thisYear;
        this.totalExpense = totalExpense;
        this.dailyAverageExpense= dailyAverageExpense;
    }
}

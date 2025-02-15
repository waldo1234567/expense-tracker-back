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

    public double getThisYear() {
        return thisYear;
    }

    public void setThisYear(double thisYear) {
        this.thisYear = thisYear;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getDailyAverageExpense() {
        return dailyAverageExpense;
    }

    public void setDailyAverageExpense(double dailyAverageExpense) {
        this.dailyAverageExpense = dailyAverageExpense;
    }
}

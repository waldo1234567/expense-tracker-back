package com.example.expense.expense_tracker.expense_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MonthlyDTO {
    @JsonProperty("year")
    private int year;
    @JsonProperty("month")
    private int month;
    @JsonProperty("totalAmount")
    private double totalAmount;

    public MonthlyDTO(int year, int month, double totalAmount){
        this.year = year;
        this.month = month;
        this.totalAmount = totalAmount;
    }

}

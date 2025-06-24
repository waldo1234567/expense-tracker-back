package com.example.expense.expense_tracker.expense_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearlyIncomeDTO {
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("category")
    private String category;
}

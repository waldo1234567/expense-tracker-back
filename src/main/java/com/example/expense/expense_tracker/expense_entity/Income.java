package com.example.expense.expense_tracker.expense_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("source")
    private String source;
    @JsonProperty("date")
    private LocalDate date;

    public Income(Double amount, String source , LocalDate date){
        this.amount = amount;
        this.source = source;
        this.date = date;
    }

}

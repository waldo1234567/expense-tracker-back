package com.example.expense.expense_tracker.expense_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Data
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @SequenceGenerator(
        name="expense sequence",
        sequenceName = "expense_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy =GenerationType.SEQUENCE,
        generator = "expense_sequence"
    )

    @JsonProperty("id")
    private long id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("category")
    private String category;

    @JsonProperty("date")
    private LocalDateTime date;

    public Expense(){
        this.date = LocalDateTime.now();
    }
    public Expense(String description, Double amount, String category, LocalDateTime date) {
        this.description = description;
        this.amount = amount;
        this.category = category.toLowerCase();
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Expense:{" +
                "id:" + id+
                ",description : " + description+
                ",amount: " + amount+
                ",category: " + category+
                "date: " + date+
                "}";
    }
}

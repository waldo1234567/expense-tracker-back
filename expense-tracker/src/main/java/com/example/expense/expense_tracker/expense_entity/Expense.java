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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

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

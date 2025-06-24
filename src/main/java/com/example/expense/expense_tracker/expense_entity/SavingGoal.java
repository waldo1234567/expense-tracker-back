package com.example.expense.expense_tracker.expense_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table
@Data
@AllArgsConstructor
@Builder
public class SavingGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("target")
    private BigDecimal targetNetBalance;

    @JsonProperty("current")
    private BigDecimal currentNetBalance;

    public SavingGoal() {
        this.currentNetBalance = BigDecimal.ZERO; // Initialize
    }

    public SavingGoal(String name, BigDecimal targetNetBalance, BigDecimal currentNetBalance){
        this.name = name;
        this.targetNetBalance = targetNetBalance;
        this.currentNetBalance = currentNetBalance;
    }

    @Override
    public String toString() {
        return "SavingGoal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", targetNetBalance=" + targetNetBalance +
                ", currentNetBalance=" + currentNetBalance +
                '}';
    }
}

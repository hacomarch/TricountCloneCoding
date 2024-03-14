package org.example.tricountcloneproject.entity;

import lombok.Data;

import java.util.List;

@Data
public class SettlementExpense {
    private Settlement settlement;
    private List<Expense> expenses;
}

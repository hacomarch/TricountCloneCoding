package org.example.tricountcloneproject.expense;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Expense {
    private Long expenseId;
    private Long memberId;
    private Long settlementId;
    private String name;
    private BigDecimal amount;
    private LocalDate expenseDate;
}

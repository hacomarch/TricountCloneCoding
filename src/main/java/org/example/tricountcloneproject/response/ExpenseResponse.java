package org.example.tricountcloneproject.response;

import lombok.Data;
import org.example.tricountcloneproject.expense.Expense;

import java.time.LocalDate;

@Data
public class ExpenseResponse {
    private String settlementName;
    private String nickname;
    private String expenseName;
    private int expenseAmount;
    private LocalDate expenseDate;

    public ExpenseResponse(String settlementName, String nickname, Expense expense) {
        this.settlementName = settlementName;
        this.nickname = nickname;
        this.expenseName = expense.getName();
        this.expenseAmount = expense.getAmount().intValue();
        this.expenseDate = expense.getExpenseDate();
    }
}

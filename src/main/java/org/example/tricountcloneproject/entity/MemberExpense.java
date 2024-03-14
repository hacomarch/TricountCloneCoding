package org.example.tricountcloneproject.entity;

import lombok.Data;

import java.util.List;

@Data
public class MemberExpense {
    private Member member;
    private List<Expense> expenses;
}

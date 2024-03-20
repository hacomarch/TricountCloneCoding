package org.example.tricountcloneproject.expense;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.expense.Expense;
import org.example.tricountcloneproject.expense.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public void insert(Long memberId, Long settlementId, Expense expense) {
        expenseRepository.save(memberId, settlementId, expense);
    }

    public void delete(Long expenseId) {
        expenseRepository.delete(expenseId);
    }

    public Expense findById(Long expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NoSuchElementException("Cannot Find Expense By ExpenseId"));
    }

    public List<Expense> findByMemberId(Long memberId) {
        return expenseRepository.findByMemberId(memberId);
    }

    public List<Expense> findBySettlementId(Long settlementId, Long memberId) {
        List<Expense> expenses = expenseRepository.findBySettlementId(settlementId, memberId);
        boolean match = expenses.stream()
                .anyMatch(expense -> expense.getMemberId().equals(memberId));
        if (!match) {
            throw new IllegalStateException("Only participants involved in the settlement can view expenses.");
        }
        return expenses;
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

}
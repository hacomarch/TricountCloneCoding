package org.example.tricountcloneproject.expense;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.exception.ExpenseAccessDeniedException;
import org.example.tricountcloneproject.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() -> new EntityNotFoundException("Expense"));
    }

    public List<Expense> findByMemberId(Long memberId) {
        return expenseRepository.findByMemberId(memberId);
    }

    public List<Expense> findBySettlementId(Long settlementId, Long memberId) {
        List<Expense> expenses = expenseRepository.findBySettlementId(settlementId, memberId);
        boolean match = expenses.stream()
                .anyMatch(expense -> expense.getMemberId().equals(memberId));
        if (!match) {
            throw new ExpenseAccessDeniedException();
        }
        return expenses;
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

}

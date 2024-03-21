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



    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

}

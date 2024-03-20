package org.example.tricountcloneproject.service;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.entity.Expense;
import org.example.tricountcloneproject.repository.ExpenseRepository;
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
        return expenseRepository.findById(expenseId).get();
    }

    public List<Expense> findByMemberId(Long memberId) {
        return expenseRepository.findByMemberId(memberId);
    }

    public List<Expense> findBySettlementId(Long settlementId, Long memberId) {
        return expenseRepository.findBySettlementId(settlementId, memberId);
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

}

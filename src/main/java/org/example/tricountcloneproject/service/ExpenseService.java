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

    public void insert(Long member_id, Long settlement_id, Expense expense) {
        expenseRepository.save(member_id, settlement_id, expense);
    }

    public void delete(Long id) {
        expenseRepository.delete(id);
    }

    public Expense findById(Long id) {
        return expenseRepository.findById(id).get();
    }

    public List<Expense> findByMemberId(Long id) {
        return expenseRepository.findByMemberId(id);
    }

    public List<Expense> findBySettlementId(Long id) {
        return expenseRepository.findBySettlementId(id);
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

}

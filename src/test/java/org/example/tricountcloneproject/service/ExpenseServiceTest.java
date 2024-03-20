package org.example.tricountcloneproject.service;

import org.example.tricountcloneproject.expense.Expense;
import org.example.tricountcloneproject.expense.ExpenseService;
import org.example.tricountcloneproject.member.Member;
import org.example.tricountcloneproject.member.MemberService;
import org.example.tricountcloneproject.settlement.Settlement;
import org.example.tricountcloneproject.settlement.SettlementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpenseServiceTest {

    @Autowired
    ExpenseService expenseService;
    @Autowired
    MemberService memberService;
    @Autowired
    SettlementService settlementService;

    @Test
    @DisplayName("Save")
    void save() {
        int listSize = expenseService.findAll().size();

        Expense expense = new Expense();
        expense.setName("산낙지");
        expense.setAmount(BigDecimal.valueOf(30000));
        expense.setExpenseDate(LocalDate.now());
        expenseService.insert(1L, 3L, expense);

        assertEquals(listSize + 1, expenseService.findAll().size());
    }

    @Test
    @DisplayName("Delete")
    void delete() {
        int listSize = expenseService.findAll().size();

        expenseService.delete(5L);

        assertEquals(listSize - 1, expenseService.findAll().size());
    }

    @Test
    @DisplayName("FindByID")
    void findById() {
        Expense findExpense = expenseService.findById(1L);

        assertEquals(1L, findExpense.getExpenseId());
        assertEquals(1L, findExpense.getMemberId());
        assertEquals(1L, findExpense.getSettlementId());
        assertEquals("숙소", findExpense.getName());
        assertEquals(String.valueOf(150000.0), findExpense.getAmount().toString());
        assertEquals(LocalDate.of(2024, 3, 5), findExpense.getExpenseDate());
    }

    @Test
    @DisplayName("FindByMemberId")
    void findByMemberId() {
        Member member = memberService.findById(1L);
        List<Expense> findExpenseByMemberId = expenseService.findByMemberId(member.getMemberId());
        findExpenseByMemberId.stream()
                .map(Expense::getMemberId)
                .forEach(memberId -> assertEquals(member.getMemberId(), memberId));
    }

    @Test
    @DisplayName("FindBySettlementId")
    void findBySettlementId() {
        Settlement settlement = settlementService.findById(1L);
        List<Expense> findExpenseBySettlementId =
                expenseService.findBySettlementId(settlement.getSettlementId(), 1L);
        findExpenseBySettlementId.stream()
                .map(Expense::getSettlementId)
                .forEach(settlement_id -> assertEquals(settlement.getSettlementId(), settlement_id));
    }

    @Test
    @DisplayName("FindAll")
    void findAll() {
        //현재 DB에 정산1, 정산3에 대한 지출만 있다.
        List<Expense> settlementList1 = expenseService.findBySettlementId(1L, 1L);
        List<Expense> settlementList3 = expenseService.findBySettlementId(3L, 1L);

        assertEquals(settlementList1.size() + settlementList3.size(),
                expenseService.findAll().size());
    }
}
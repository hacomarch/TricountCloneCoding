package org.example.tricountcloneproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.entity.Expense;
import org.example.tricountcloneproject.entity.Member;
import org.example.tricountcloneproject.entity.SessionConst;
import org.example.tricountcloneproject.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @ResponseBody
    @PostMapping("/{settlement_id}/expense/save")
    public String save(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
                       Member loginMember,
                       @PathVariable Long settlement_id, @RequestBody Expense expense) {
        expenseService.insert(loginMember.getMember_id(), settlement_id, expense);
        return "expense save ok";
    }

    @ResponseBody
    @GetMapping("/expense/delete/{expense_id}")
    public String delete(@PathVariable Long expense_id) {
        expenseService.delete(expense_id);
        return "expense delete ok";
    }

    @ResponseBody
    @GetMapping("/expense/{expense_id}")
    public Expense findById(@PathVariable Long expense_id) {
        return expenseService.findById(expense_id);
    }

    @ResponseBody
    @GetMapping("/member/expenseList")
    public List<Expense> findByMemberId(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember) {
        return expenseService.findByMemberId(loginMember.getMember_id());
    }

    @ResponseBody
    @GetMapping("/settlement/{settlement_id}/expenseList")
    public List<Expense> findBySettlementId(@PathVariable Long settlement_id) {
        return expenseService.findBySettlementId(settlement_id);
    }

    @ResponseBody
    @GetMapping("/expense/list")
    public List<Expense> findAll() {
        return expenseService.findAll();
    }
}

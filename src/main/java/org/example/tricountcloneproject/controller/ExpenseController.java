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
    @DeleteMapping("/expense/{expense_id}")
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
    public Object findBySettlementId(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember,
            @PathVariable Long settlement_id) {
        try {
            return expenseService.findBySettlementId(settlement_id, loginMember.getMember_id());
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    @ResponseBody
    @GetMapping("/expense/list")
    public List<Expense> findAll() {
        return expenseService.findAll();
    }
}

package org.example.tricountcloneproject.expense;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.member.Member;
import org.example.tricountcloneproject.member.SessionConst;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @ResponseBody
    @PostMapping("/settlement/{settlementId}/expense/add")
    public String save(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
                       Member loginMember,
                       @PathVariable Long settlementId, @RequestBody Expense expense) {
        expenseService.insert(loginMember.getMemberId(), settlementId, expense);
        return "expense save ok";
    }

    @ResponseBody
    @DeleteMapping("/expense/{expenseId}")
    public String delete(@PathVariable Long expenseId) {
        expenseService.delete(expenseId);
        return "expense delete ok";
    }

    @ResponseBody
    @GetMapping("/expense/{expenseId}")
    public Expense findById(@PathVariable Long expenseId) {
        return expenseService.findById(expenseId);
    }

    @ResponseBody
    @GetMapping("/member/expenses")
    public List<Expense> findExpensesByMemberId(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember) {
        return expenseService.findByMemberId(loginMember.getMemberId());
    }

    @ResponseBody
    @GetMapping("/settlement/{settlementId}/expenses")
    public Object findExpensesBySettlementId(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember,
            @PathVariable Long settlementId) {
        try {
            return expenseService.findBySettlementId(settlementId, loginMember.getMemberId());
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    @ResponseBody
    @GetMapping("/expenses")
    public List<Expense> findAll() {
        return expenseService.findAll();
    }
}

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
    @GetMapping("/expenses")
    public List<Expense> findAll() {
        return expenseService.findAll();
    }
}

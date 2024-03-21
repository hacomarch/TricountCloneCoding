package org.example.tricountcloneproject.member;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.exception.IncorrectPasswordException;
import org.example.tricountcloneproject.exception.EntityNotFoundException;
import org.example.tricountcloneproject.exception.UserNotFoundException;
import org.example.tricountcloneproject.expense.Expense;
import org.example.tricountcloneproject.response.ExpenseResponse;
import org.example.tricountcloneproject.settlement.Settlement;
import org.example.tricountcloneproject.settlement.SettlementRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SettlementRepository settlementRepository;

    public void insert(Member member) {
        memberRepository.save(member);
    }

    public void delete(Long memberId) {
        memberRepository.delete(memberId);
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member"));
    }

    public List<Settlement> findSettlementsById(Long memberId) {
        return memberRepository.findSettlementsById(memberId);
    }

    public List<ExpenseResponse> findExpensesById(Long memberId) {
        List<Expense> expenses = memberRepository.findExpensesById(memberId);

        return expenses.stream().map(expense -> {
            String settlementName = settlementRepository.findById(expense.getSettlementId())
                    .orElseThrow(() -> new EntityNotFoundException("Settlement"))
                    .getName();
            String nickname = memberRepository.findNicknameById(expense.getMemberId());
            return new ExpenseResponse(settlementName, nickname, expense);
        }).collect(Collectors.toList());
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member login(String userId, String userPw) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        if (member.getUserPw().equals(userPw)) {
            return member;
        } else {
            throw new IncorrectPasswordException();
        }
    }
}

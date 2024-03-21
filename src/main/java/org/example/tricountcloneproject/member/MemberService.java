package org.example.tricountcloneproject.member;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.exception.IncorrectPasswordException;
import org.example.tricountcloneproject.exception.EntityNotFoundException;
import org.example.tricountcloneproject.exception.UserNotFoundException;
import org.example.tricountcloneproject.expense.Expense;
import org.example.tricountcloneproject.settlement.Settlement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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

    public List<Expense> findExpensesById(Long memberId) {
        return memberRepository.findExpensesById(memberId);
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

package org.example.tricountcloneproject.member;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.member.Member;
import org.example.tricountcloneproject.settlement.Settlement;
import org.example.tricountcloneproject.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
                .orElseThrow(() -> new NoSuchElementException("Cannot Find Member by memberId"));
    }

    public List<Settlement> findSettlementsById(Long memberId) {
        return memberRepository.findSettlementsById(memberId);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member login(String userId, String userPw) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("Unregistered User"));

        if (member.getUserPw().equals(userPw)) {
            return member;
        } else {
            throw new IllegalStateException("Password Incorrect");
        }
    }
}

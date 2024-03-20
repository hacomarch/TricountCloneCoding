package org.example.tricountcloneproject.service;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.entity.Member;
import org.example.tricountcloneproject.entity.Settlement;
import org.example.tricountcloneproject.repository.MemberRepository;
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
        return memberRepository.findById(memberId).get();
    }

    public List<Settlement> findSettlementsById(Long memberId) {
        return memberRepository.findSettlementsById(memberId);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member login(String userId, String userPw) {
        Member member = memberRepository.findByUserId(userId, userPw).get();

        if (member.getUserPw().equals(userPw)) {
            return member;
        } else {
            throw new IllegalStateException("Password Incorrect");
        }
    }
}

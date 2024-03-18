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

    public void delete(Long id) {
        memberRepository.delete(id);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).get();
    }

    public List<Settlement> findSettlementsById(Long id) {
        return memberRepository.findSettlementListById(id);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member login(String user_id, String user_pw) {
        return memberRepository.authenticate(user_id, user_pw).get();
    }
}

package org.example.tricountcloneproject.service;

import org.example.tricountcloneproject.member.Member;
import org.example.tricountcloneproject.member.MemberService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    static Member member1;
    static Member member2;
    static Member member3;

    @BeforeAll
    static void init() {
        member1 = new Member();
        member1.setUserId("admin");
        member1.setUserPw("1212");
        member1.setNickname("mum");

        member2 = new Member();
        member2.setUserId("haco");
        member2.setUserPw("11222");
        member2.setNickname("ahahah");

        member3 = new Member();
        member3.setUserId("sungtae");
        member3.setUserPw("0124");
        member3.setNickname("vivi");
    }



    @Test
    @DisplayName("Save")
    void save() {
        assertEquals(0, memberService.findAll().size());

        memberService.insert(member1);
        memberService.insert(member2);
        memberService.insert(member3);

        assertEquals(3, memberService.findAll().size());
    }

    @Test
    @DisplayName("Delete")
    void delete() {
        assertEquals(3, memberService.findAll().size());

        memberService.delete(3L);

        assertEquals(2, memberService.findAll().size());
    }

    @Test
    @DisplayName("FindById")
    void findById() {
        Member findMember = memberService.findById(1L);

        assertEquals(1L, findMember.getMemberId());
        assertEquals("admin", findMember.getUserId());
        assertEquals("1212", findMember.getUserPw());
        assertEquals("mum", findMember.getNickname());
    }

    @Test
    @DisplayName("FindAll")
    void findAll() {
        assertEquals(2, memberService.findAll().size());
    }
}
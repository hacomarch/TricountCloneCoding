package org.example.tricountcloneproject.service;

import org.example.tricountcloneproject.entity.Member;
import org.example.tricountcloneproject.repository.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
        member1.setUser_id("admin");
        member1.setUser_pw("1212");
        member1.setNickname("mum");

        member2 = new Member();
        member2.setUser_id("haco");
        member2.setUser_pw("11222");
        member2.setNickname("ahahah");

        member3 = new Member();
        member3.setUser_id("sungtae");
        member3.setUser_pw("0124");
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

        assertEquals(1L, findMember.getMember_id());
        assertEquals("admin", findMember.getUser_id());
        assertEquals("1212", findMember.getUser_pw());
        assertEquals("mum", findMember.getNickname());
    }

    @Test
    @DisplayName("FindAll")
    void findAll() {
        assertEquals(2, memberService.findAll().size());
    }
}
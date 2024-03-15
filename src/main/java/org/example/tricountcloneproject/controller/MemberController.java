package org.example.tricountcloneproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tricountcloneproject.entity.Member;
import org.example.tricountcloneproject.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ResponseBody
    @PostMapping("/member/save")
    public String save(@RequestBody Member member) {
        memberService.insert(member);
        return "member save ok";
    }

    @ResponseBody
    @GetMapping("/member/delete/{id}")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "member delete ok";
    }

    @ResponseBody
    @GetMapping("/member/{id}")
    public Member findById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @ResponseBody
    @GetMapping("/member/list")
    public List<Member> findAll() {
        return memberService.findAll();
    }
}

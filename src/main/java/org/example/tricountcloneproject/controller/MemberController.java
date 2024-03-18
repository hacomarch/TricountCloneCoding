package org.example.tricountcloneproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tricountcloneproject.entity.Login;
import org.example.tricountcloneproject.entity.Member;
import org.example.tricountcloneproject.entity.SessionConst;
import org.example.tricountcloneproject.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ResponseBody
    @PostMapping("/login")
    public String login(@RequestBody Login login, HttpServletRequest request) {
        try {
            Member loginMember = memberService.login(login.getUser_id(), login.getUser_pw());

            if (loginMember == null) {
                return "Unregistered User";
            }

            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
            session.getAttributeNames().asIterator()
                    .forEachRemaining(name ->
                            log.info("session name={}, value={}", name, session.getAttribute(name)));

        } catch (IllegalStateException e) {
            return e.getMessage();
        }
        return "Success Login";
    }

    @ResponseBody
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            return "Success Logout";
        }
        return "Session is Null";
    }

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

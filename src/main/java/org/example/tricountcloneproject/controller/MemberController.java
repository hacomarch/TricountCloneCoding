package org.example.tricountcloneproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tricountcloneproject.entity.Login;
import org.example.tricountcloneproject.entity.Member;
import org.example.tricountcloneproject.entity.SessionConst;
import org.example.tricountcloneproject.entity.Settlement;
import org.example.tricountcloneproject.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @ResponseBody
    @PostMapping("/login")
    public String login(@RequestBody Login login, HttpServletRequest request) {
        try {
            Member loginMember = memberService.login(login.getUserId(), login.getUserPw());

            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
            session.getAttributeNames()
                    .asIterator()
                    .forEachRemaining(name -> log.info("session name={}, value={}",
                            name, session.getAttribute(name)));
        } catch (IllegalStateException | NoSuchElementException e) {
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
    @PostMapping("/join")
    public String save(@RequestBody Member member) {
        memberService.insert(member);
        return "member save ok";
    }

    @ResponseBody
    @DeleteMapping("/{memberId}")
    public String delete(@PathVariable Long memberId) {
        memberService.delete(memberId);
        return "member delete ok";
    }

    @ResponseBody
    @GetMapping("/{memberId}")
    public Member findById(@PathVariable Long memberId) {
        return memberService.findById(memberId);
    }

    @ResponseBody
    @GetMapping("/{memberId}/settlements")
    public List<Settlement> findSettlementsById(@PathVariable Long memberId) {
        return memberService.findSettlementsById(memberId);
    }

    @ResponseBody
    @GetMapping
    public List<Member> findAll() {
        return memberService.findAll();
    }
}

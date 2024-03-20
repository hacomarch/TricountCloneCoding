package org.example.tricountcloneproject.member;

import lombok.Data;

@Data
public class Member {
    private Long memberId;
    private String userId;
    private String userPw;
    private String nickname;
}

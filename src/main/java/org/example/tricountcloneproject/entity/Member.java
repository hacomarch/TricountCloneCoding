package org.example.tricountcloneproject.entity;
import lombok.Data;

@Data
public class Member {
    private Long member_id;
    private String user_id;
    private String password;
    private String nickname;
}

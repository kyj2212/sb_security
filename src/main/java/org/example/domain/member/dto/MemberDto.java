package org.example.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.example.domain.member.entity.Member;

@Getter
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String name;
    private boolean loginStatus;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.name = member.getName();
        this.loginStatus = false;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

}
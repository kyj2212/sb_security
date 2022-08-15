package org.example.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.member.entity.Member;
import org.example.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String member_name, String id, String member_pwd, String member_email, Boolean member_login_status) {
        Member member = new Member();
        member.setMemberName(member_name);
        member.setMemberId(id);
        member.setMemberPwd(passwordEncoder.encode(member_pwd));
        member.setMemberEmail(member_email);
        member.setLoginStatus(member_login_status);
        this.memberRepository.save(member);
        return member;
    }
}
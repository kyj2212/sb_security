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

}
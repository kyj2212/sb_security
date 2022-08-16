package org.example.domain.member.service;

import java.util.Collections;

import org.example.domain.member.entity.Member;
import org.example.domain.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findByUsername(username) // optional 이므로 member가 null이면 exception
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("일치하는 계정이 없습니다"));
    }


    private UserDetails createUserDetails(Member member) {
        final GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());
        // TOKEN, AUTHENTICATION 에 넣을 값 (ex. username, id)
        return new User(
                String.valueOf(member.getId()), // 왜 id 를 넣을까? username을 넣어야 하는거 아닌가?
             //   member.getUsername(),
                member.getPassword(),
                Collections.singleton(grantedAuthority));
    }


}

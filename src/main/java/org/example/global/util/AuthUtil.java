package org.example.global.util;

import lombok.RequiredArgsConstructor;
import org.example.domain.member.entity.Member;
import org.example.domain.member.exception.MemberDoesNotExistException;
import org.example.domain.member.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final MemberRepository memberRepository;

    public Long getLoginMemberIdOrNull() {
        try {
            final String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return Long.valueOf(memberId);
        } catch (Exception e) {
            return -1L;
        }
    }

    public Long getLoginMemberId() {
        try {
            final String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return Long.valueOf(memberId);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Member getLoginMember() {
        try {
            final Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            return memberRepository.findById(memberId).orElseThrow(MemberDoesNotExistException::new);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
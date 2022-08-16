package org.example.domain.member.repository;

import org.example.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findById(Long id);

    boolean existsByUsername(String username);

    List<Member> findAllByUsernameIn(Collection<String> usernames);

    List<Member> findAllByIdIn(Collection<Long> ids);

}

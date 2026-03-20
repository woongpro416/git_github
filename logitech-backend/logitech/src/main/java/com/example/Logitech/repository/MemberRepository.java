package com.example.Logitech.repository;

import com.example.Logitech.domain.Member;
import com.example.Logitech.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String LoginId);

    Optional<Member> findByEmail(String email);

    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String email);

    List<Member> findByRole(Role role);

    List<Member> findByNameContainingOrLoginIdContaining(String name, String loginId);

}

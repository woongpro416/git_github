package com.example.Logitech.service;

import com.example.Logitech.domain.Cart;
import com.example.Logitech.domain.Member;
import com.example.Logitech.domain.Role;
import com.example.Logitech.dto.MemberJoinRequestDto;
import com.example.Logitech.dto.MemberLoginRequestDto;
import com.example.Logitech.dto.MemberResponseDto;
import com.example.Logitech.dto.MemberUpdateDto;
import com.example.Logitech.repository.CartRepository;
import com.example.Logitech.repository.ItemRepository;
import com.example.Logitech.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)

public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    //회원가입 처리
    @Transactional
    public Long join(MemberJoinRequestDto request) {
        if(memberRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        if(!request.isPasswordMatch()) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        Member member = request.toEntity();
        memberRepository.save(member).getMemberId();
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        memberRepository.save(member);


        // 회원가입 시 장바구니 자동 생성
        Cart cart = Cart.builder()
                .member(member)
                .build();
        cartRepository.save(cart);
        return member.getMemberId();
    }


    //관리자 등록
    public void addManager(String name, String loginId, String password) {
        Member member = Member.builder()
                .name(name)
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .build();
        memberRepository.save(member);
    }

    //아이디 중복확인
    public boolean checkLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }


    //로그인 처리
    public MemberResponseDto login(MemberLoginRequestDto request) {
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalStateException("아이디 또는 비밀번호가 일치하지 않습니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return new MemberResponseDto(member);
    }

    //회원 목록 조회
    public List<MemberResponseDto> getMemberList() {
        return memberRepository.findAll()
                .stream()
                .map(member -> new MemberResponseDto(member))
                .collect(Collectors.toList());
    }

    //회원 상세 조회
    public MemberResponseDto getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        return new MemberResponseDto(member);
    }

    //회원 정보 변경
    @Transactional
    public void updateMember(Long memberId, MemberUpdateDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        if(!request.isPasswordMatch()) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        member.setName(request.getName());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setPhone(request.getPhone());
        member.setAddress(request.getAddress());
        member.setEmail(request.getEmail());
    }

    //회원 탈퇴
    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        memberRepository.deleteById(memberId);
    }

    public MemberResponseDto getMemberByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        return new MemberResponseDto(member);
    }

    public String getPasswordByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."))
                .getPassword();
    }

    //회원 검색
    public List<MemberResponseDto> searchMember(String keyword) {
        return memberRepository.findByNameContainingOrLoginIdContaining(keyword, keyword)
                .stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

    //회원 등급 변경
    @Transactional
    public void changeRole(Long memberId, Role role) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        member.setRole(role);
    }

    //관리자 대시보드
    public Long getMemberCount() {
        return memberRepository.count();
    }
}

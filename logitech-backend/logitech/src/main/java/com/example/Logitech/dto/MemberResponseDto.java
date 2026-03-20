package com.example.Logitech.dto;

import com.example.Logitech.domain.Member;
import com.example.Logitech.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class MemberResponseDto {

    private Long memberId;

    private String name;

    private String loginId;

    private String email;

    private String phone;

    private String address;

    private LocalDateTime createdAt;

    private Role role;

    //Entity -> Dto
    public MemberResponseDto(Member member) {
        this.memberId = member.getMemberId();
        this.name = member.getName();
        this.loginId = member.getLoginId();
        this.createdAt = member.getCreateAt();
        this.role = member.getRole();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.address = member.getAddress();
    }

}

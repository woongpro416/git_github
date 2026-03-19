package com.example.Logitech.dto;

import com.example.Logitech.domain.Member;
import com.example.Logitech.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MemberJoinRequestDto {


    @NotBlank(message = "이름은 필수 입력입니다.")
    @Size(min = 2, max = 20, message = "이름은 2~20자 이내로 입력하세요.")
    private String name;

    @NotBlank(message = "아이디 필수 입력")
    @Size(min = 4, max = 20, message = "아이디는 4~20자 이내로 입력하세요.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내로 입력하세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력입니다.")
    private String passwordConfirm;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String phone;

    private String address;

    //비밀번호 확인 일치 여부
    public boolean isPasswordMatch() {
        return password != null && password.equals(passwordConfirm);
    }


    //DTO -> Entity로 변환
    public Member toEntity() {
        return Member.builder()
                .name(name)
                .loginId(loginId)
                .password(password)
                .email(email)
                .phone(phone)
                .address(address)
                .role(Role.USER)
                .build();
    }

}

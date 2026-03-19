package com.example.Logitech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {

    @NotBlank(message = "이름은 입력해주세요.")
    @Size(min = 2, max = 20, message = "이름은 2~20자 이내로 입력하세요.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max= 20, message = "비밀번호는 8~20자 이내로 입력하세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;

    private String phone;
    private String address;
    private String email;

    public boolean isPasswordMatch() {
        return password != null && password.equals(passwordConfirm);
    }
}

package com.group2.blogplatform.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {

    @NotBlank(message="Không được để trống")
    @Size( max = 50,message = "Không vượt quá 50 ký tự")
    private String username;

    @NotBlank(message="Không được để trống")
    @Email
    private String email;

    @NotBlank(message="Không được để trống")
    @Size(min = 6, max = 30,message = "Mật khẩu ít nhất 6 ký tự, không vượt quá 30 ký tự")
    private String password;
}
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

    @NotBlank(message ="Không được để trống")
    @Size(max = 30, message ="Độ dài tối đa 30 ký tự")
    private String username;

    @NotBlank(message ="Không được để trống")
    @Email
    private String email;

    @NotBlank(message ="Không được để trống")
    @Size(min = 6, max = 30, message ="Độ dài ít nhất 6 ký tự, nhiều nhất 30 ký tự")
    private String password;
}
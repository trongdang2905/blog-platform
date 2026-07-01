package com.group2.blogplatform.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {
    //size
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
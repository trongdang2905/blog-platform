package com.group2.blogplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportRequest {

    private Long postId;

    private Long commentId;

    @NotBlank(message = "Please provide a reason for the report")
    private String reason;

    private String returnUrl;
}

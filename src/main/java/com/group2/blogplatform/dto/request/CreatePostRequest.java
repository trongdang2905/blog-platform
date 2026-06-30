package com.group2.blogplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {

    @NotBlank(message = "Title is not blank")
    private String title;

    @NotBlank(message = "Content is not blank")
    private String content;

    private MultipartFile image;

    @NotNull(message = "You have to select topic")
    private Long topicId;

}

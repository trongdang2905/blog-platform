package com.group2.blogplatform.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    private Long userId;
    private String title;
    private String content;
    private String imageUrl;
    private Long topicId;
}

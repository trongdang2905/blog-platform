package com.group2.blogplatform.dto.response;

import com.group2.blogplatform.entity.Comment;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String content;
    private String topicName;
    private boolean isPinned;
    private String duration;
    private boolean saved;
    private List<Comment> comments;
    private String imageUrl;
}

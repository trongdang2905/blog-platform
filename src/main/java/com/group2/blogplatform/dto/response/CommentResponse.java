package com.group2.blogplatform.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse implements Comparable<CommentResponse> {
    private boolean success;
    private String message;

    private Long id;
    private String content;
    private String createdAt;
    private Long userId;
    private String username;
    private boolean ownedByCurrentUser;

    @Override
    public int compareTo(CommentResponse o) {
        return this.getId().compareTo(o.getId());
    }
}
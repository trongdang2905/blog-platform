package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.response.ToggleLikeResponse;

public interface LikeService {

    ToggleLikeResponse like(Long postId, Long userId);

    ToggleLikeResponse unlike(Long postId, Long userId);

    long countLikes(Long postId);

    boolean isLikedByCurrentUser(Long postId, Long userId);
}
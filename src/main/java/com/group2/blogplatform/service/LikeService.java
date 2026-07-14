package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.response.ToggleLikeResponse;

public interface LikeService {

    ToggleLikeResponse like(Long postId);

    ToggleLikeResponse unlike(Long postId);

    long countLikes(Long postId);

    boolean isLikedByCurrentUser(Long postId);
}
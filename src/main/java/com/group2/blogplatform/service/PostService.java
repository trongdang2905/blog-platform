package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.request.CreatePostRequest;
import com.group2.blogplatform.dto.response.CreatePostResponse;

public interface PostService {
    CreatePostResponse createPost(CreatePostRequest createPostRequest);
}

package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.request.CreatePostRequest;
import com.group2.blogplatform.dto.response.CreatePostResponse;
import com.group2.blogplatform.dto.response.PostDTO;
import com.group2.blogplatform.entity.SavedPost;
import com.group2.blogplatform.exception.ExcessImageException;


import java.io.IOException;
import java.util.List;

public interface PostService {
    CreatePostResponse createPost(CreatePostRequest createPostRequest) throws IOException, ExcessImageException;

    List<PostDTO> getPosts(Long currentPage);

    SavedPost savePost(Long userId, Long postId);

    SavedPost unsavePost(Long userId, Long postId);

    List<PostDTO> searchPosts(String keyword);

    PostDTO getPost(Long postId);

    long countPosts();
}

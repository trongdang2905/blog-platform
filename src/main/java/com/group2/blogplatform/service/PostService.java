package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.request.CreatePostRequest;
import com.group2.blogplatform.dto.response.CreatePostResponse;
import com.group2.blogplatform.dto.response.PostDTO;
import com.group2.blogplatform.dto.response.SaveResponse;
import com.group2.blogplatform.entity.SavedPost;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.exception.ExcessImageException;


import java.io.IOException;
import java.util.List;

public interface PostService {
    CreatePostResponse createPost(User user, CreatePostRequest createPostRequest) throws IOException, ExcessImageException;

    List<PostDTO> getPosts(Long currentPage);

    List<PostDTO> getSavedPosts(Long userID);

    SaveResponse savePost(Long userId, Long postId);

    SaveResponse unsavePost(Long userId, Long postId);

    List<PostDTO> searchPosts(String keyword);

    PostDTO getPost(Long postId);

    long countPosts();
}

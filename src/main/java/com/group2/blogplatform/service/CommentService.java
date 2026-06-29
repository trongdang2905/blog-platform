package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.request.CreateCommentRequest;
import com.group2.blogplatform.dto.response.CommentResponse;
import com.group2.blogplatform.dto.response.CreateCommentResponse;

import java.util.List;

public interface CommentService {

    CreateCommentResponse createComment(CreateCommentRequest request);

    List<CommentResponse> getVisibleCommentsByPost(Long postId);

    long countVisibleComments(Long postId);

    CommentResponse createCommentToAppend(CreateCommentRequest request);
}

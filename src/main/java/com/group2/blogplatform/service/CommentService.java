package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.request.CreateCommentRequest;
import com.group2.blogplatform.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {

    // Tao comment moi cho bai viet, gan cho userId truyen vao (lay tu session)
    CommentResponse createComment(CreateCommentRequest request, Long userId);

    // Lay danh sach comment dang hien thi (VISIBLE) cua 1 bai viet
    // currentUserId co the null (khach chua dang nhap) -> dung de danh dau comment nao la cua chinh minh
    List<CommentResponse> getVisibleCommentsByPost(Long postId, Long currentUserId);

    long countVisibleComments(Long postId);
}
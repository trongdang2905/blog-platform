package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.request.CreateCommentRequest;
import com.group2.blogplatform.dto.response.CommentResponse;
import com.group2.blogplatform.entity.Comment;
import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.StatusComment;
import com.group2.blogplatform.entity.StatusPost;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.repository.CommentRepository;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.UserRepository;
import com.group2.blogplatform.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentResponse createComment(CreateCommentRequest request, Long userId) {
        if (request.getContent() == null || request.getContent().isBlank()) {
            return CommentResponse.builder()
                    .success(false)
                    .message("Comment content must not be empty")
                    .build();
        }

        Post post = postRepository.findById(request.getPostId()).orElse(null);
        if (post == null) {
            return CommentResponse.builder()
                    .success(false)
                    .message("Post not found")
                    .build();
        }

        if (post.getStatusPost() != StatusPost.PUBLISHED) {
            return CommentResponse.builder()
                    .success(false)
                    .message("This post is no longer available")
                    .build();
        }

        User user = userRepository.findByID(userId);
        if (user == null) {
            return CommentResponse.builder()
                    .success(false)
                    .message("User not found")
                    .build();
        }

        Comment comment = new Comment();
        comment.setContent(request.getContent().trim());
        comment.setPost(post);
        comment.setUser(user);
        comment.setStatusComment(StatusComment.VISIBLE);
        commentRepository.save(comment);

        return CommentResponse.builder()
                .success(true)
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(formatTime(comment.getCreatedAt()))
                .userId(user.getId())
                .username(user.getUsername())
                .ownedByCurrentUser(true)
                .build();
    }

    @Override
    public List<CommentResponse> getVisibleCommentsByPost(Long postId, Long currentUserId) {
        List<Comment> comments = commentRepository.findVisibleByPostId(postId, StatusComment.VISIBLE);
        return comments.stream()
                .map(c -> CommentResponse.builder()
                        .success(true)
                        .id(c.getId())
                        .content(c.getContent())
                        .createdAt(formatTime(c.getCreatedAt()))
                        .userId(c.getUser().getId())
                        .username(c.getUser().getUsername())
                        .ownedByCurrentUser(c.getUser().getId().equals(currentUserId))
                        .build()
                ).toList();
    }

    @Override
    public long countVisibleComments(Long postId) {
        return commentRepository.countByPost_IdAndStatusComment(postId, StatusComment.VISIBLE);
    }

    private String formatTime(LocalDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }
}
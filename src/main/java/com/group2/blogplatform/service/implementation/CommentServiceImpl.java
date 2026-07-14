package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.request.CreateCommentRequest;
import com.group2.blogplatform.dto.response.CommentResponse;
import com.group2.blogplatform.dto.response.CreateCommentResponse;
import com.group2.blogplatform.entity.Comment;
import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.StatusComment;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.repository.CommentRepository;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.UserRepository;
import com.group2.blogplatform.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // TODO: thay bang user dang dang nhap (Spring Security) khi co UC Authentication
    private static final Long CURRENT_USER_ID = 1L;

    @Override
    public CreateCommentResponse createComment(CreateCommentRequest request) {
        if (request.getContent() == null || request.getContent().isBlank()) {
            return new CreateCommentResponse(false, "Comment content must not be empty");
        }

        Post post = postRepository.findById(request.getPostId()).orElse(null);
        if (post == null) {
            return new CreateCommentResponse(false, "Post not found");
        }

        User user = userRepository.findByID(CURRENT_USER_ID);
        if (user == null) {
            return new CreateCommentResponse(false, "User not found");
        }

        Comment comment = new Comment();
        comment.setContent(request.getContent().trim());
        comment.setPost(post);
        comment.setUser(user);
        comment.setStatusComment(StatusComment.VISIBLE);

        commentRepository.save(comment);
        return new CreateCommentResponse(true, "Comment posted successfully");
    }

    @Override
    public List<CommentResponse> getVisibleCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findVisibleByPostId(postId, StatusComment.VISIBLE);
        return comments.stream()
                .map(c -> CommentResponse.builder()
                        .success(true)
                        .id(c.getId())
                        .content(c.getContent())
                        .createdAt(formatTime(c.getCreatedAt()))
                        .userId(c.getUser().getId())
                        .username(c.getUser().getUsername())
                        .ownedByCurrentUser(c.getUser().getId().equals(CURRENT_USER_ID))
                        .build()
                ).toList();

    }

    @Override
    public long countVisibleComments(Long postId) {
        return commentRepository.countByPost_IdAndStatusComment(postId, StatusComment.VISIBLE);
    }

    @Override
    public CommentResponse createCommentToAppend(CreateCommentRequest request) {
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

        User user = userRepository.findByID(CURRENT_USER_ID);
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
                .userId(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .ownedByCurrentUser(false)
                .build();
    }

    private String formatTime(LocalDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }
}
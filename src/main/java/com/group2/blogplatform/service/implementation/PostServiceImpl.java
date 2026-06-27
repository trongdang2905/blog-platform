package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.request.*;
import com.group2.blogplatform.dto.response.*;
import com.group2.blogplatform.entity.*;
import com.group2.blogplatform.repository.*;
import com.group2.blogplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final SavedPostRepository savedPostRepository;


    @Override
    public CreatePostResponse createPost(CreatePostRequest createPostRequest) {
        // Hard code vi chua co login
        User user = userRepository.findByID(1L);
        if (user == null) {
            return new CreatePostResponse(false, "User not found");
        }

        Topic topic = topicRepository.findByID(createPostRequest.getTopicId());

        Post post = new Post().builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .imageUrl(createPostRequest.getImageUrl())
                .statusPost(StatusPost.PUBLISHED)
                .topic(topic)
                .user(user)
                .build();
        postRepository.save(post);
        return new CreatePostResponse(true, "Post created successfully");
    }

    @Override
    public List<PostDTO> getPosts(Long currentPage) {
        long size = postRepository.countPost();
        Pageable pageable = PageRequest.of(0, 10);
        int totalPages = (int) Math.ceil((double) size / 10);
        if (totalPages == currentPage) {
            pageable = PageRequest.of(0, (int) (size - (currentPage - 1) * 10));
        }

        List<PostDTO> list = postRepository.getPostByCursor(currentPage * 10 + 1, pageable)
                .stream()
                .map(post ->
                        new PostDTO().builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .isPinned(post.isPinned())
                                .username(post.getUser().getUsername())
                                .duration(calculateDuration(post.getCreatedAt()))
                                .topicName(post.getTopic().getName())
                                .saved(checkSaved(1L, post.getId()))
                                .build())
                .toList();

        return list;
    }

    @Override
    public SavedPost savePost(Long userId, Long postId) {
        User user = userRepository.findByID(userId);
        Post post = postRepository.findByID(postId);
        SavedPost savedPost = new SavedPost()
                .builder()
                .post(post)
                .user(user)
                .build();
        savedPostRepository.save(savedPost);
        return savedPost;
    }

    @Override
    public SavedPost unsavePost(Long userId, Long postId) {
        SavedPost savedPost = savedPostRepository.findByUserIdAndPostId(userId, postId);
        if (savedPost == null) {
            return null;
        }
        savedPostRepository.delete(savedPost);
        return savedPost;
    }

    @Override
    public List<PostDTO> searchPosts(String keyword) {
        List<PostDTO> list = postRepository.searchPostByTitleOrContent(keyword)
                .stream()
                .map(post -> new PostDTO().builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .isPinned(post.isPinned())
                        .username(post.getUser().getUsername())
                        .duration(calculateDuration(post.getCreatedAt()))
                        .topicName(post.getTopic().getName())
                        .saved(checkSaved(1L, post.getId()))
                        .build())
                .toList();
        return list;
    }

    @Override
    public PostDTO getPost(Long postId) {
        Post post = postRepository.findByID(postId);
        PostDTO dto = new PostDTO().builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .isPinned(post.isPinned())
                .username(post.getUser().getUsername())
                .duration(calculateDuration(post.getCreatedAt()))
                .topicName(post.getTopic().getName())
                .saved(checkSaved(1L, post.getId()))
                .comments(post.getComments())
                .imageUrl(post.getImageUrl())
                .build();
        return dto;
    }

    private String calculateDuration(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        return String.format("%s giờ trước", duration.toHours());
    }

    private boolean checkSaved(Long userId, Long postId) {
        return savedPostRepository.checkSavedByUserIdAndPostId(userId, postId);
    }
}

package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.request.*;
import com.group2.blogplatform.dto.response.*;
import com.group2.blogplatform.entity.*;
import com.group2.blogplatform.exception.ExcessImageException;
import com.group2.blogplatform.repository.*;
import com.group2.blogplatform.service.ImageKitService;
import com.group2.blogplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final SavedPostRepository savedPostRepository;
    private final ImageKitService imageKitService;


    @Override
    public CreatePostResponse createPost(User user, CreatePostRequest createPostRequest) throws IOException, ExcessImageException {

        if (user == null) {
            return new CreatePostResponse(false, "User not found");
        }

        Topic topic = topicRepository.findByID(createPostRequest.getTopicId());

        Post post = new Post().builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .imageUrl(imageKitService.uploadImage(createPostRequest.getImage()))
                .statusPost(StatusPost.PUBLISHED)
                .topic(topic)
                .user(user)
                .build();
        postRepository.save(post);
        return new CreatePostResponse(true, "Post created successfully");
    }

    @Override
    public List<PostDTO> getPosts(Long page) {

        Pageable pageable = PageRequest.of(page.intValue() - 1, 10);

        List<PostDTO> list = postRepository.getPostByPinnedAndCreated(pageable)
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
                                .build())
                .toList();

        return list;
    }

    @Override
    public List<PostDTO> getSavedPosts(Long userID) {
        List<Post> list = savedPostRepository.findAllByUserId(userID)
                .stream()
                .map(savedPost -> savedPost.getPost()).toList();
        List<PostDTO> postDto = list
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
                                .build())
                .toList();
        return postDto;
    }

    @Override
    public SaveResponse savePost(Long userId, Long postId) {

        if (savedPostRepository.checkSavedByUserIdAndPostId(userId, postId)) {
            return new SaveResponse(false, "You have already saved post!");
        }

        User user = userRepository.findByID(userId);

        if (user == null) {
            return new SaveResponse(false, "User not found!");
        }
        Post post = postRepository.findByID(postId);
        if (post == null) {
            return new SaveResponse(false, "Post not found!");
        }
        SavedPost savedPost = new SavedPost()
                .builder()
                .post(post)
                .user(user)
                .build();
        savedPostRepository.save(savedPost);
        return new SaveResponse(true, "Post saved successfully!");
    }

    @Override
    public SaveResponse unsavePost(Long userId, Long postId) {
        SavedPost savedPost = savedPostRepository.findByUserIdAndPostId(userId, postId);
        if (savedPost == null) {
            return new SaveResponse(false, "Can not find saved post!");
        }
        savedPostRepository.delete(savedPost);
        return new SaveResponse(true, "Post unsaved successfully!");
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
                .comments(post.getComments())
                .imageUrl(post.getImageUrl())
                .build();
        return dto;
    }

    @Override
    public long countPosts() {
        return postRepository.countPost();
    }

    private String calculateDuration(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-YY"));
    }


}

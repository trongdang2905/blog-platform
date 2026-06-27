package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.response.PostDTO;
import com.group2.blogplatform.entity.Topic;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.SavedPostRepository;
import com.group2.blogplatform.repository.TopicRepository;
import com.group2.blogplatform.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final PostRepository postRepository;
    private final SavedPostRepository savedPostRepository;

    @Override
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    @Override
    public List<PostDTO> findAllPostsByTopic(Long currentPage, Long topicId) {
        long size = postRepository.countPost();
        Pageable pageable = PageRequest.of(0, 10);
        int totalPages = (int) Math.ceil((double) size / 10);
        if (totalPages == currentPage) {
            pageable = PageRequest.of(0, (int) (size - (currentPage - 1) * 10));
        }
        List<PostDTO> list = postRepository.getPostByTopicIDCursor(currentPage * 10 + 1, topicId, pageable)
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

    private String calculateDuration(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        return String.format("%s giờ trước", duration.toHours());
    }

    private boolean checkSaved(Long userId, Long postId) {
        return savedPostRepository.checkSavedByUserIdAndPostId(userId, postId);
    }
}

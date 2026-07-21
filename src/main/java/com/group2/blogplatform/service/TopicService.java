package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.response.PostDTO;
import com.group2.blogplatform.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TopicService {
    List<Topic> findAllWithActive();
    List<Topic> findAll();

    Page<Topic> findAll(Pageable pageable);

    List<PostDTO> findAllPostsByTopic(Long currentPage, Long topicId);
    void saveTopic(Topic topic);
    Topic getTopicById(Integer id);
    void deleteTopic(Integer id);

    List<Topic> searchTopics(String keyword);

    Page<Topic> searchTopics(String keyword, Pageable pageable);
}
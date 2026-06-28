package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.response.PostDTO;
import com.group2.blogplatform.entity.Topic;
import java.util.List;

public interface TopicService {
    List<Topic> findAll();
    List<PostDTO> findAllPostsByTopic(Long currentPage, Long topicId);

    void saveTopic(Topic topic);
    Topic getTopicById(Integer id);
    void deleteTopic(Integer id);
}
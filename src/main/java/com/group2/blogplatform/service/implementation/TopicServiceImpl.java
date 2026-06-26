package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.entity.Topic;
import com.group2.blogplatform.repository.TopicRepository;
import com.group2.blogplatform.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }
}

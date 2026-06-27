package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.response.PostDTO;
import com.group2.blogplatform.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/{id}")
    public String getPostByTopic(@PathVariable("id") Long topicId, Model model) {
        List<PostDTO> posts = topicService.findAllPostsByTopic(1L, topicId);
        model.addAttribute("posts", posts);
        return "home";
    }
}

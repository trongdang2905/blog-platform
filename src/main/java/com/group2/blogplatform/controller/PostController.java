package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.request.CreatePostRequest;
import com.group2.blogplatform.dto.response.CreatePostResponse;
import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.Topic;
import com.group2.blogplatform.service.PostService;
import com.group2.blogplatform.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("user/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final TopicService topicService;

    @GetMapping("/create")
    public String getViewPost(Model model) {
        List<Topic> topics = topicService.findAll();
        model.addAttribute("topics", topics);
        return "create-post";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute CreatePostRequest dto, Model model) {
        CreatePostResponse response = postService.createPost(dto);
        model.addAttribute("response", response);
        List<Topic> topics = topicService.findAll();
        model.addAttribute("topics", topics);
        return "create-post";
    }

}

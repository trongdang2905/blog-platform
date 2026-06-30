package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.response.PostDTO;
import com.group2.blogplatform.entity.Topic;
import com.group2.blogplatform.service.PostService;
import com.group2.blogplatform.service.TopicService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;
    private final TopicService topicService;

    @GetMapping("/")
    public String getHome(Model model,
                          HttpSession session,
                          @RequestParam(defaultValue = "1") int currentPage) {
        int pageSize = 10;
        List<PostDTO> posts = postService.getPosts((long) currentPage);

        long totalPosts = postService.countPosts();
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);


        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);

        List<Topic> topics = topicService.findAllWithActive();
        session.setAttribute("topics", topics);
        return "member/home";
    }

}

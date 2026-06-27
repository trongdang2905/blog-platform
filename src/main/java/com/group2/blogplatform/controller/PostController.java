package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.request.CreatePostRequest;
import com.group2.blogplatform.dto.response.CreatePostResponse;
import com.group2.blogplatform.dto.response.PostDTO;
import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.Topic;
import com.group2.blogplatform.service.PostService;
import com.group2.blogplatform.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("user/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/create")
    public String getViewPost(Model model) {
        return "create-post";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute CreatePostRequest dto, Model model) {
        CreatePostResponse response = postService.createPost(dto);
        model.addAttribute("response", response);
        return "create-post";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Void> savePost(@RequestParam(name = "postId") Long postId, Model model) {
        postService.savePost(1L, postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsave")
    @ResponseBody
    public ResponseEntity<Void> unsavePost(@RequestParam(name = "postId") Long postId, Model model) {
        postService.unsavePost(1L, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public String searchPost(@RequestParam(name = "keyword") String keyword, Model model) {
        List<PostDTO> posts = postService.searchPosts(keyword);
        model.addAttribute("keyword", keyword);
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/{id}")
    public String getDetailPost(@PathVariable("id") Long postId, Model model) {
        PostDTO post = postService.getPost(postId);
        model.addAttribute("post", post);
        return "post-detail";
    }


}

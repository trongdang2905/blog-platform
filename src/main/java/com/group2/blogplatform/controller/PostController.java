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
import com.group2.blogplatform.service.CommentService;
import com.group2.blogplatform.service.LikeService;

import java.util.List;

@Controller
@RequestMapping("user/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // UC04/UC05 - bo sung de lay du lieu comment/like cho trang chi tiet bai viet
    private final CommentService commentService;
    private final LikeService likeService;

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

        // UC04/UC05 - bo sung du lieu de hien thi comment, like, report tren trang chi tiet
        model.addAttribute("postId", postId);
        model.addAttribute("comments", commentService.getVisibleCommentsByPost(postId));
        model.addAttribute("commentCount", commentService.countVisibleComments(postId));
        model.addAttribute("likeCount", likeService.countLikes(postId));
        model.addAttribute("likedByCurrentUser", likeService.isLikedByCurrentUser(postId));

        return "post-detail";
    }


}

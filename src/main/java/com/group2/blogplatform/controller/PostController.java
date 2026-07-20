package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.request.CreatePostRequest;
import com.group2.blogplatform.dto.response.CreatePostResponse;
import com.group2.blogplatform.dto.response.PostDTO;
import com.group2.blogplatform.dto.response.SaveResponse;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.exception.ExcessImageException;
import com.group2.blogplatform.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.group2.blogplatform.service.CommentService;
import com.group2.blogplatform.service.LikeService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("user/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    @GetMapping("/create")
    public String getViewPost(Model model) {
        model.addAttribute("dto", new CreatePostRequest());
        return "member/create-post";
    }

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("dto") CreatePostRequest dto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) throws IOException, ExcessImageException {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "member/create-post";
        }
        CreatePostResponse response = postService.createPost(user, dto);
        model.addAttribute("response", response);
        return "member/create-post";
    }

    @GetMapping("/save")
    public String savePost(@RequestParam(name = "postId") Long postId,
                           HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        SaveResponse response = postService.savePost(user.getId(), postId);
        redirectAttributes.addFlashAttribute("response", response);
        return "redirect:/user/post/" + postId;
    }

    @GetMapping("/unsave")
    public String unsavePost(@RequestParam(name = "postId") Long postId,
                             Model model,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        SaveResponse response = postService.unsavePost(user.getId(), postId);
        model.addAttribute("response", response);
        List<PostDTO> posts = postService.getSavedPosts(user.getId());
        model.addAttribute("posts", posts);
        return "member/saved-post";
    }

    @GetMapping("/saved-posts")
    public String getSavedPosts(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        List<PostDTO> posts = postService.getSavedPosts(user.getId());
        model.addAttribute("posts", posts);
        return "member/saved-post";
    }

    @GetMapping("/search")
    public String searchPost(@RequestParam(name = "keyword") String keyword, Model model) {
        List<PostDTO> posts = postService.searchPosts(keyword);
        model.addAttribute("keyword", keyword);
        model.addAttribute("posts", posts);
        return "member/home";
    }

    @GetMapping("/{id}")
    public String getDetailPost(@PathVariable("id") Long postId, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        Long currentUserId = (currentUser != null) ? currentUser.getId() : null;

        PostDTO post = postService.getPost(postId);
        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.getVisibleCommentsByPost(postId, currentUserId));
        model.addAttribute("commentCount", commentService.countVisibleComments(postId));
        model.addAttribute("likeCount", likeService.countLikes(postId));
        model.addAttribute("likedByCurrentUser", likeService.isLikedByCurrentUser(postId, currentUserId));
        return "member/post-detail";
    }
}
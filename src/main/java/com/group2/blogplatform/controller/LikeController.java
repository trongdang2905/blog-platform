package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.response.ToggleLikeResponse;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.service.LikeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/post")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // UC04 - Member Like: bam tim de like/unlike bai viet, goi qua AJAX (fetch/JS)
    @PostMapping("/like")
    public ToggleLikeResponse like(@RequestParam("postId") Long postId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ToggleLikeResponse(false, false, likeService.countLikes(postId), "Please log in to like posts");
        }
        return likeService.like(postId, currentUser.getId());
    }

    @PostMapping("/unlike")
    public ToggleLikeResponse unlike(@RequestParam("postId") Long postId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ToggleLikeResponse(false, false, likeService.countLikes(postId), "Please log in to like posts");
        }
        return likeService.unlike(postId, currentUser.getId());
    }
}
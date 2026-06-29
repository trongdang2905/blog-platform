package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.response.ToggleLikeResponse;
import com.group2.blogplatform.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/post")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // UC04 - Member Like: bam tim de like/unlike bai viet, goi qua AJAX (fetch/JS)
    @PostMapping("/like")
    public ToggleLikeResponse toggleLike(@RequestParam("postId") Long postId) {
        return likeService.toggleLike(postId);
    }

    @PostMapping("/unlike")
    public ToggleLikeResponse toggleUnLike(@RequestParam("postId") Long postId) {
        return likeService.toggleLike(postId);
    }
}

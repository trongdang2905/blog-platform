package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.response.ToggleLikeResponse;
import com.group2.blogplatform.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/post")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // UC04 - Member Like: bam tim de like/unlike bai viet, goi qua AJAX (fetch/JS)
    @PostMapping("/{postId}/like")
    public ToggleLikeResponse toggleLike(@PathVariable("postId") Long postId) {
        return likeService.toggleLike(postId);
    }
}

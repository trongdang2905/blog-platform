package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.request.CreateCommentRequest;
import com.group2.blogplatform.dto.response.CreateCommentResponse;
import com.group2.blogplatform.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("user/post")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // UC04 - Member Comment: gui binh luan moi cho bai viet
    @PostMapping("/{postId}/comments")
    public String createComment(@PathVariable("postId") Long postId,
                                  @ModelAttribute CreateCommentRequest dto,
                                  RedirectAttributes redirectAttributes) {
        dto.setPostId(postId);
        CreateCommentResponse response = commentService.createComment(dto);

        redirectAttributes.addFlashAttribute("commentSuccess", response.isSuccess());
        redirectAttributes.addFlashAttribute("commentMessage", response.getMessage());

        return "redirect:/user/post/" + postId;
    }
}

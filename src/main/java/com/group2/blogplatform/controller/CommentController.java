package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.request.CreateCommentRequest;
import com.group2.blogplatform.dto.response.CommentResponse;
import com.group2.blogplatform.dto.response.CreateCommentResponse;
import com.group2.blogplatform.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("user/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // UC04 - Member Comment: gui binh luan moi cho bai viet
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<CommentResponse> createComment(@ModelAttribute CreateCommentRequest dto,
                                                         RedirectAttributes redirectAttributes) {

        CommentResponse commentResponse = commentService.createCommentToAppend(dto);

        return ResponseEntity.ok(commentResponse);
    }
}

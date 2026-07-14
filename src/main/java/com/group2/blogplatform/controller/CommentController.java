package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.request.CreateCommentRequest;
import com.group2.blogplatform.dto.response.CommentResponse;
import com.group2.blogplatform.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // UC04 - Member Comment: gui binh luan moi cho bai viet
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<CommentResponse> createComment(@Valid @ModelAttribute CreateCommentRequest dto,
                                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            CommentResponse errorResponse = CommentResponse.builder()
                    .success(false)
                    .message(bindingResult.getFieldError().getDefaultMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }

        CommentResponse commentResponse = commentService.createCommentToAppend(dto);

        if (!commentResponse.isSuccess()) {
            return ResponseEntity.badRequest().body(commentResponse);
        }

        return ResponseEntity.ok(commentResponse);
    }
}
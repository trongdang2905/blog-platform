package com.group2.blogplatform.exception;

import com.group2.blogplatform.dto.response.CreatePostResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IOException.class)
    public String handleImageException(IOException e, Model model){
        CreatePostResponse response = new CreatePostResponse(false, e.getMessage());
        model.addAttribute("response", response);
        return "member/create-post";
    }

    @ExceptionHandler(ExcessImageException.class)
    public String handleMaxImageException(ExcessImageException e, Model model){
        CreatePostResponse response = new CreatePostResponse(false, e.getMessage());
        model.addAttribute("response", response);
        return "member/create-post";
    }
}

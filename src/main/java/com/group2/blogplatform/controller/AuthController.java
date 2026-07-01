package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.request.UserRegisterRequest;
import com.group2.blogplatform.dto.request.UserRequest;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @GetMapping("/login")
    public String login() {
        return "view/auth/login";
    }


    @PostMapping("/login")
    public ModelAndView processLogin(
            @Valid @ModelAttribute UserRequest userRequest,
            BindingResult result,
            HttpSession session )
    {
        if (result.hasErrors()) {
            return new ModelAndView("view/auth/login");
        }
            ModelAndView mv = new ModelAndView();
            User user = authService.authenticate(userRequest);
            session.setAttribute("user", user);
            mv.setViewName("redirect:/");
            return mv;
        }


    @GetMapping("/register")
    public String register() {
        return "view/auth/register";
    }

    @PostMapping("/register")
    public ModelAndView processRegister(
            @Valid @ModelAttribute("userRegisterRequest") UserRegisterRequest request,
            BindingResult result)
    {
        if (result.hasErrors()) {
            return new ModelAndView("view/auth/register");
        }
        authService.register(request);
        return new ModelAndView("redirect:/auth/login");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

}

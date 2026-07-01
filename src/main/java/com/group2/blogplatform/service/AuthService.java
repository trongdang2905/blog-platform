package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.request.UserRegisterRequest;
import com.group2.blogplatform.dto.request.UserRequest;
import com.group2.blogplatform.entity.User;

public interface AuthService {
    User authenticate(UserRequest userRequest);

    void register(UserRegisterRequest registerRequest);
}
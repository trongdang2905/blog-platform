package com.group2.blogplatform.service;

import com.group2.blogplatform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(String search, String status);

    Page<User> getAllUsers(String search, String status, Pageable pageable);

    void updateUserRole(Integer id, String role);
    void toggleUserStatus(Integer id);
}
package com.group2.blogplatform.service;

import com.group2.blogplatform.entity.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers(String search, String status);
    void updateUserRole(Integer id, String role);
    void toggleUserStatus(Integer id);
}
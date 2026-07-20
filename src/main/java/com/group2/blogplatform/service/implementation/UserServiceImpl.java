package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.entity.Role;
import com.group2.blogplatform.entity.StatusUser;
import com.group2.blogplatform.repository.UserRepository;
import com.group2.blogplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers(String search, String status) {
        // Lấy dữ liệu từ Database
        List<User> users = userRepository.findAll();

        // 1. Logic tìm kiếm theo Username hoặc Email
        if (search != null && !search.trim().isEmpty()) {
            String lowerSearch = search.toLowerCase();
            users = users.stream()
                    .filter(u -> (u.getUsername() != null && u.getUsername().toLowerCase().contains(lowerSearch))
                            || (u.getEmail() != null && u.getEmail().toLowerCase().contains(lowerSearch)))
                    .collect(Collectors.toList());
        }

        // 2. Logic lọc theo Trạng thái (ACTIVE / BANNED)
        if (status != null && !status.trim().isEmpty()) {
            try {
                StatusUser filterStatus = StatusUser.valueOf(status.toUpperCase());
                users = users.stream()
                        .filter(u -> u.getStatusUser() == filterStatus)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                // Bỏ qua nếu bộ lọc không khớp
            }
        }

        return users;
    }

    @Override
    public void updateUserRole(Integer id, String role) {
        User user = userRepository.findById(Long.valueOf(id)).orElse(null);
        if (user != null) {
            try {
                user.setRole(Role.valueOf(role.toUpperCase()));
                userRepository.save(user);
            } catch (IllegalArgumentException e) {
                System.err.println("Lỗi map enum role: " + e.getMessage());
            }
        }
    }

    @Override
    public void toggleUserStatus(Integer id) {
        User user = userRepository.findById(Long.valueOf(id)).orElse(null);
        if (user != null) {
            if (user.getStatusUser() == StatusUser.ACTIVE) {
                user.setStatusUser(StatusUser.BANNED);
            } else {
                user.setStatusUser(StatusUser.ACTIVE);
            }
            userRepository.save(user);
        }
    }
}
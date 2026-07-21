package com.group2.blogplatform.controller;

import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    // 1. Endpoint hiển thị danh sách người dùng (có phân trang, tìm kiếm & lọc status)
    @GetMapping
    public String listUsers(@RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "status", required = false) String status,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "4") int size,
                            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> userPage = userService.getAllUsers(search, status, pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("status", status);

        return "admin/user/list";
    }

    // 2. Xử lý cập nhật Role nhận ID qua PathVariable
    @PostMapping("/update-role/{id}")
    public String updateUserRole(@PathVariable("id") Integer id,
                                 @RequestParam("role") String role) {
        userService.updateUserRole(id, role);
        return "redirect:/admin/users";
    }

    // 3. Xử lý Khóa/Mở khóa nhận ID qua PathVariable
    @PostMapping("/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable("id") Integer id) {
        userService.toggleUserStatus(id);
        return "redirect:/admin/users";
    }
}
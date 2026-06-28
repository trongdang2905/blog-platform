package com.group2.blogplatform.controller;

import com.group2.blogplatform.entity.Topic;
import com.group2.blogplatform.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/topics")
@RequiredArgsConstructor
public class AdminTopicController {

    private final TopicService topicService;

    // 1. Hiển thị danh sách tất cả các Topic trong trang Admin
    @GetMapping
    public String listTopics(Model model) {
        List<Topic> topics = topicService.findAll();
        model.addAttribute("topics", topics);
        return "admin/topic/list";
    }

    // 2. Hiển thị form để thêm mới một Topic
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("topic", new Topic());
        return "admin/topic/create";
    }

    // 3. Xử lý lưu dữ liệu thêm mới (có check lỗi Validation bằng @Valid)
    @PostMapping("/create")
    public String createTopic(@Valid @ModelAttribute("topic") Topic topic, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/topic/create";
        }
        topicService.saveTopic(topic);
        return "redirect:/admin/topics";
    }

    // 4. Hiển thị form để sửa một Topic theo ID
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        try {
            Topic topic = topicService.getTopicById(id);
            model.addAttribute("topic", topic);
            return "admin/topic/edit";
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/topics";
        }
    }

    // 5. Xử lý cập nhật dữ liệu sau khi sửa
    @PostMapping("/edit/{id}")
    public String updateTopic(@PathVariable("id") Integer id, @Valid @ModelAttribute("topic") Topic topic, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/topic/edit";
        }
        topic.setId(id);
        topicService.saveTopic(topic);
        return "redirect:/admin/topics";
    }

    // 6. Xử lý xóa một Topic
    @GetMapping("/delete/{id}")
    public String deleteTopic(@PathVariable("id") Integer id) {
        try {
            topicService.deleteTopic(id);
        } catch (Exception e) {
            // Xử lý nếu dính ràng buộc dữ liệu bài viết
        }
        return "redirect:/admin/topics";
    }
}
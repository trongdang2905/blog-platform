package com.group2.blogplatform.controller;

import com.group2.blogplatform.entity.Report;
import com.group2.blogplatform.entity.Role;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.entity.StatusReport;
import com.group2.blogplatform.repository.UserRepository; // Nhớ import UserRepository của ông vào đây
import com.group2.blogplatform.service.ModerationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/moderation")
public class ModerationWebController {

    private final ModerationService moderationService;
    private final UserRepository userRepository; // 1. Khai báo thêm UserRepository ở đây

    // 2. Cập nhật Constructor để Spring tự động Inject cả 2 thằng vào
    public ModerationWebController(ModerationService moderationService, UserRepository userRepository) {
        this.moderationService = moderationService;
        this.userRepository = userRepository;
    }

    // Trang hiển thị danh sách báo cáo vi phạm
    @GetMapping("/reports")
    public String showPendingReports(Model model) {
        List<Report> reports = moderationService.getPendingReports();
        model.addAttribute("reports", reports);
        return "moderation/reports";
    }

    // Xử lý khi bấm nút Duyệt trên giao diện -> ĐÃ TỰ ĐỘNG HOÁ ID
    @PostMapping("/reports/{id}/resolve")
    public String resolveReport(@PathVariable Long id) {

        // 3. TỰ ĐỘNG: Lấy ông User đầu tiên có quyền MODERATOR trong Database ra để xử lý
        // (Thay phương thức tìm kiếm tương ứng với hàm ông viết trong UserRepository, ví dụ: findByRole)
        User mockModerator = userRepository.findByRole(Role.MODERATOR)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lỗi rồi ông ơi: Database chưa có tài khoản nào mang quyền MODERATOR để test cả!"));

        Long realModeratorId = mockModerator.getId(); // Lấy ID tự động (2L, 3L hay 4L nó tự bắt hết)

        // Truyền ID tự động tìm được vào Service
        moderationService.reviewReport(id, realModeratorId, StatusReport.RESOLVED);

        // Xử lý xong thì tự động tải lại trang danh sách
        return "redirect:/moderation/reports";
    }

    // Xử lý khi bấm nút Ghim bài viết trên giao diện
    @PostMapping("/posts/{id}/pin")
    public String pinPost(@PathVariable Long id) {
        moderationService.togglePinPost(id);
        return "redirect:/moderation/reports";
    }
}
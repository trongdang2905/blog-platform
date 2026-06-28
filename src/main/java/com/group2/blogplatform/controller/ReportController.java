package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.request.CreateReportRequest;
import com.group2.blogplatform.dto.response.CreateReportResponse;
import com.group2.blogplatform.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("user/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // UC05 - Member Report Content: bao cao bai viet hoac binh luan vi pham
    // dto.postId hoac dto.commentId duoc gui kem qua form (hidden input), chi mot trong hai duoc set
    @PostMapping("/create")
    public String createReport(@ModelAttribute CreateReportRequest dto,
                                 RedirectAttributes redirectAttributes) {
        CreateReportResponse response = reportService.createReport(dto);

        redirectAttributes.addFlashAttribute("reportSuccess", response.isSuccess());
        redirectAttributes.addFlashAttribute("reportMessage", response.getMessage());

        // Quay lai trang nguoi dung dang xem (truyen kem qua hidden input returnUrl trong form)
        if (dto.getReturnUrl() != null && !dto.getReturnUrl().isBlank()) {
            return "redirect:" + dto.getReturnUrl();
        }
        if (dto.getPostId() != null) {
            return "redirect:/user/post/" + dto.getPostId();
        }
        return "redirect:/";
    }
}

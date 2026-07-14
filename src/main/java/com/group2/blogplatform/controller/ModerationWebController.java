package com.group2.blogplatform.controller;

import com.group2.blogplatform.entity.Report;
import com.group2.blogplatform.entity.Role;
import com.group2.blogplatform.entity.StatusReport;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.UserRepository;
import com.group2.blogplatform.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/moderation")
@RequiredArgsConstructor
public class ModerationWebController {

    private final ModerationService moderationService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * Moderator Dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("pendingReports",
                moderationService.countPendingReports());

        model.addAttribute("resolvedReports",
                moderationService.countResolvedReports());

        model.addAttribute("pinnedPosts",
                moderationService.countPinnedPosts());

        return "moderation/moderator-dashboard";
    }

    /**
     * Report List
     */
    @GetMapping("/reports")
    public String reportedPosts(Model model) {

        model.addAttribute("reports",
                moderationService.getPendingReports());

        return "moderation/reported-posts";
    }

    /**
     * Report Detail
     */
    @GetMapping("/reports/{id}")
    public String reportDetail(@PathVariable Long id,
                               Model model) {

        Report report = moderationService.getReportById(id);

        model.addAttribute("report", report);

        return "moderation/report-detail";
    }

    /**
     * Post List
     */
    @GetMapping("/posts")
    public String postList(Model model) {

        model.addAttribute("posts",
                postRepository.findAll());

        return "moderation/post-list";
    }

    /**
     * Resolve Report (Hide post + Resolve report)
     */
    @PostMapping("/reports/{id}/resolve")
    public String resolveReport(@PathVariable Long id) {

        User moderator = userRepository.findByRole(Role.MODERATOR)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "The database does not contain any MODERATOR accounts, so this feature cannot be tested at the moment."
                ));

        moderationService.reviewReport(
                id,
                moderator.getId(),
                StatusReport.RESOLVED
        );

        return "redirect:/moderation/reports";
    }

    /**
     * Hide Post
     */
    @PostMapping("/posts/{id}/hide")
    public String hidePost(@PathVariable Long id) {

        moderationService.hidePost(id);

        return "redirect:/moderation/posts";
    }

    /**
     * Pin / Unpin Post
     */
    @PostMapping("/posts/{id}/pin")
    public String pinPost(@PathVariable Long id) {

        moderationService.togglePinPost(id);

        return "redirect:/moderation/posts";
    }

}
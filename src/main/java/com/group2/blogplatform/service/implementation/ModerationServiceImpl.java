package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.Report;
import com.group2.blogplatform.entity.StatusPost;
import com.group2.blogplatform.entity.StatusReport;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.ReportRepository;
import com.group2.blogplatform.repository.UserRepository;
import com.group2.blogplatform.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerationService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Get all pending reports
     */
    @Override
    public List<Report> getPendingReports() {
        return reportRepository.findByStatusReport(StatusReport.PENDING);
    }

    /**
     * Get report detail
     */
    @Override
    public Report getReportById(Long reportId) {

        return reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new RuntimeException("Report not found with ID: " + reportId));
    }

    /**
     * Review report
     */
    @Override
    @Transactional
    public Report reviewReport(Long reportId,
                               Long moderatorId,
                               StatusReport newStatus) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new RuntimeException("Report not found with ID: " + reportId));

        User moderator = userRepository.findById(moderatorId)
                .orElseThrow(() ->
                        new RuntimeException("Moderator not found with ID: " + moderatorId));

        report.setStatusReport(newStatus);
        report.setModerator(moderator);

        Report updatedReport = reportRepository.save(report);

        // Khi resolve thì tự động hide bài viết
        if (newStatus == StatusReport.RESOLVED && report.getPost() != null) {
            hidePost(report.getPost().getId());
        }

        return updatedReport;
    }

    /**
     * Hide post
     */
    @Override
    @Transactional
    public void hidePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found with ID: " + postId));

        post.setStatusPost(StatusPost.HIDDEN);

        postRepository.save(post);
    }

    /**
     * Pin / Unpin post
     */
    @Override
    @Transactional
    public Post togglePinPost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found with ID: " + postId));

        post.setPinned(!post.isPinned());

        return postRepository.save(post);
    }

    /**
     * Dashboard statistics
     */
    @Override
    public long countPendingReports() {
        return reportRepository.findByStatusReport(StatusReport.PENDING).size();
    }

    @Override
    public long countResolvedReports() {
        return reportRepository.findByStatusReport(StatusReport.RESOLVED).size();
    }

    @Override
    public long countPinnedPosts() {

        return postRepository.findAll()
                .stream()
                .filter(Post::isPinned)
                .count();
    }

}
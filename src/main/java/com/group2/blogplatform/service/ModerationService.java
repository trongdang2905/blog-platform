package com.group2.blogplatform.service;

import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.Report;
import com.group2.blogplatform.entity.StatusReport;

import java.util.List;

public interface ModerationService {

    // Report
    List<Report> getPendingReports();

    Report getReportById(Long reportId);

    Report reviewReport(Long reportId,
                        Long moderatorId,
                        StatusReport newStatus);

    // Post
    void hidePost(Long postId);

    Post togglePinPost(Long postId);

    // Dashboard
    long countPendingReports();

    long countResolvedReports();

    long countPinnedPosts();
}
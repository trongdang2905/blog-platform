package com.group2.blogplatform.service;

import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.Report;
import com.group2.blogplatform.entity.StatusReport;

import java.util.List;

public interface ModerationService {

    /**
     * UC07: Lấy danh sách các báo cáo vi phạm đang ở trạng thái chờ xử lý (PENDING)
     * @return Danh sách các đối tượng Report
     */
    List<Report> getPendingReports();

    /**
     * UC07: Duyệt báo cáo vi phạm (Chấp nhận hoặc Từ chối)
     * @param reportId ID của báo cáo cần duyệt
     * @param moderatorId ID của người kiểm duyệt (Admin/Moderator)
     * @param newStatus Trạng thái mới (APPROVED hoặc REJECTED)
     * @return Đối tượng Report sau khi đã cập nhật
     */
    Report reviewReport(Long reportId, Long moderatorId, StatusReport newStatus);

    /**
     * UC08: Chủ động ẩn một bài viết vi phạm dựa trên ID bài viết
     * @param postId ID của bài viết cần ẩn
     */
    void hidePost(Long postId);

    /**
     * UC12: Thay đổi trạng thái ghim của bài viết (Ghim hoặc Bỏ ghim)
     * @param postId ID của bài viết cần ghim/bỏ ghim
     * @return Đối tượng Post sau khi đã thay đổi trạng thái ghim
     */
    Post togglePinPost(Long postId);
}

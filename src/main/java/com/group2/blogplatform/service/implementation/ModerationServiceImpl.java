package com.group2.blogplatform.service.implementation;


import com.group2.blogplatform.entity.Report;
import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.entity.StatusReport;
import com.group2.blogplatform.entity.StatusPost;
import com.group2.blogplatform.repository.ReportRepository;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.UserRepository; // Giả định ông có UserRepository để tìm User
import com.group2.blogplatform.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerationService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public List<Report> getPendingReports() {
        // Entity của ông đặt tên trường là statusReport
        return reportRepository.findByStatusReport(StatusReport.PENDING);
    }

    @Override
    @Transactional
    public Report reviewReport(Long reportId, Long moderatorId, StatusReport newStatus) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy báo cáo với ID: " + reportId));

        // Tìm đối tượng User làm moderator
        User moderator = userRepository.findById(moderatorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người kiểm duyệt với ID: " + moderatorId));

        // Sửa theo đúng tên biến trong Entity của ông
        report.setStatusReport(newStatus);
        report.setModerator(moderator);

        Report updatedReport = reportRepository.save(report);

        // LOGIC TỰ ĐỘNG: Nếu report được xử lý là đúng (RESOLVED) và báo cáo đó gắn liền với một Post (post != null)
        if (newStatus == StatusReport.RESOLVED && report.getPost() != null) {
            hidePost(report.getPost().getId());
        }

        return updatedReport;
    }

    @Override
    @Transactional
    public void hidePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với ID: " + postId));

        // Entity của ông đặt tên trường là statusPost
        post.setStatusPost(StatusPost.HIDDEN); // Ông hãy chắc chắn trong enum StatusPost có giá trị HIDDEN nhé
        postRepository.save(post);
    }

    @Override
    @Transactional
    public Post togglePinPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với ID: " + postId));

        // Với kiểu boolean nguyên thủy, Lombok sinh hàm getter là isPinned()
        post.setPinned(!post.isPinned());

        return postRepository.save(post);
    }
}
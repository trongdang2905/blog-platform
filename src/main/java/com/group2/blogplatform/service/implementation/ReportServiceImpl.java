package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.request.CreateReportRequest;
import com.group2.blogplatform.dto.response.CreateReportResponse;
import com.group2.blogplatform.entity.Comment;
import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.Report;
import com.group2.blogplatform.entity.StatusReport;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.repository.CommentRepository;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.ReportRepository;
import com.group2.blogplatform.repository.UserRepository;
import com.group2.blogplatform.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CreateReportResponse createReport(CreateReportRequest request, Long reporterId) {
        boolean hasPost = request.getPostId() != null;
        boolean hasComment = request.getCommentId() != null;

        if (hasPost == hasComment) {
            // ca hai deu null hoac ca hai deu duoc set -> request khong hop le
            return new CreateReportResponse(false, "Report must target exactly one post or one comment");
        }

        if (request.getReason() == null || request.getReason().isBlank()) {
            return new CreateReportResponse(false, "Please provide a reason for the report");
        }

        User reporter = userRepository.findByID(reporterId);
        if (reporter == null) {
            return new CreateReportResponse(false, "User not found");
        }

        String reasonText = request.getReason().trim();
        if (request.getNote() != null && !request.getNote().isBlank()) {
            reasonText = reasonText + " - " + request.getNote().trim();
        }

        Report report = new Report();
        report.setReason(reasonText);
        report.setReporter(reporter);
        report.setStatusReport(StatusReport.PENDING);

        if (hasPost) {
            Post post = postRepository.findById(request.getPostId()).orElse(null);
            if (post == null) {
                return new CreateReportResponse(false, "Post not found");
            }
            if (reportRepository.existsByReporter_IdAndPost_Id(reporterId, post.getId())) {
                return new CreateReportResponse(false, "You have already reported this post");
            }
            report.setPost(post);
        } else {
            Comment comment = commentRepository.findById(request.getCommentId()).orElse(null);
            if (comment == null) {
                return new CreateReportResponse(false, "Comment not found");
            }
            if (reportRepository.existsByReporter_IdAndComment_Id(reporterId, comment.getId())) {
                return new CreateReportResponse(false, "You have already reported this comment");
            }
            report.setComment(comment);
        }

        reportRepository.save(report);
        return new CreateReportResponse(true, "Report submitted. Our moderators will review it soon.");
    }
}
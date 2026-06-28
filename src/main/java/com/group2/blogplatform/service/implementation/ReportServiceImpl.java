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

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // TODO: thay bang user dang dang nhap (Spring Security) khi co UC Authentication
    private static final Long CURRENT_USER_ID = 1L;

    @Override
    public CreateReportResponse createReport(CreateReportRequest request) {
        boolean hasPost = request.getPostId() != null;
        boolean hasComment = request.getCommentId() != null;

        if (hasPost == hasComment) {
            // ca hai deu null hoac ca hai deu duoc set -> sai
            return new CreateReportResponse(false, "Report must target exactly one post or one comment");
        }

        if (request.getReason() == null || request.getReason().isBlank()) {
            return new CreateReportResponse(false, "Please provide a reason for the report");
        }

        User reporter = userRepository.findByID(CURRENT_USER_ID);
        if (reporter == null) {
            return new CreateReportResponse(false, "User not found");
        }

        Report report = new Report();
        report.setReason(request.getReason().trim());
        report.setReporter(reporter);
        report.setStatusReport(StatusReport.PENDING);

        if (hasPost) {
            Post post = postRepository.findById(request.getPostId()).orElse(null);
            if (post == null) {
                return new CreateReportResponse(false, "Post not found");
            }
            if (reportRepository.existsByReporter_IdAndPost_Id(CURRENT_USER_ID, post.getId())) {
                return new CreateReportResponse(false, "You have already reported this post");
            }
            report.setPost(post);
        } else {
            Comment comment = commentRepository.findById(request.getCommentId()).orElse(null);
            if (comment == null) {
                return new CreateReportResponse(false, "Comment not found");
            }
            if (reportRepository.existsByReporter_IdAndComment_Id(CURRENT_USER_ID, comment.getId())) {
                return new CreateReportResponse(false, "You have already reported this comment");
            }
            report.setComment(comment);
        }

        reportRepository.save(report);
        return new CreateReportResponse(true, "Report submitted. Our moderators will review it soon.");
    }
}

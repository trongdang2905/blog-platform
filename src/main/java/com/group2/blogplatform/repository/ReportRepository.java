package com.group2.blogplatform.repository;

import com.group2.blogplatform.entity.Report;
import com.group2.blogplatform.entity.StatusReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByReporter_IdAndPost_Id(Long reporterId, Long postId);

    boolean existsByReporter_IdAndComment_Id(Long reporterId, Long commentId);

    List<Report> findByStatusReport(StatusReport status);
}

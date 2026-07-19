package com.group2.blogplatform.service;

import com.group2.blogplatform.dto.request.CreateReportRequest;
import com.group2.blogplatform.dto.response.CreateReportResponse;

public interface ReportService {

    CreateReportResponse createReport(CreateReportRequest request, Long reporterId);
}
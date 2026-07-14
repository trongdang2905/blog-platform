package com.group2.blogplatform.controller;

import com.group2.blogplatform.dto.request.CreateReportRequest;
import com.group2.blogplatform.dto.response.CreateReportResponse;
import com.group2.blogplatform.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // UC05 - Member Report Content: bao cao bai viet hoac binh luan vi pham
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<CreateReportResponse> createReport(@Valid @ModelAttribute CreateReportRequest dto,
                                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            CreateReportResponse errorResponse = new CreateReportResponse(false,
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        CreateReportResponse response = reportService.createReport(dto);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
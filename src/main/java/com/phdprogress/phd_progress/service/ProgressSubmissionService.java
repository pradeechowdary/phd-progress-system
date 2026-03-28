package com.phdprogress.phd_progress.service;

import com.phdprogress.phd_progress.dto.submission.ProgressSubmissionRequest;
import com.phdprogress.phd_progress.dto.submission.ProgressSubmissionResponse;
import com.phdprogress.phd_progress.dto.submission.SubmissionFileDownload;
import com.phdprogress.phd_progress.entity.SubmissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProgressSubmissionService {

    ProgressSubmissionResponse createSubmission(ProgressSubmissionRequest request);

    Page<ProgressSubmissionResponse> getAllSubmissions(SubmissionStatus status, Long studentId, Pageable pageable);

    Page<ProgressSubmissionResponse> getMySubmissions(SubmissionStatus status, Pageable pageable);

    ProgressSubmissionResponse getSubmissionById(Long id);

    ProgressSubmissionResponse updateSubmission(Long id, ProgressSubmissionRequest request);

    void deleteSubmission(Long id);

    ProgressSubmissionResponse uploadFile(Long id, MultipartFile file);

    SubmissionFileDownload downloadFile(Long id);

    ProgressSubmissionResponse submit(Long id);

    ProgressSubmissionResponse advisorApprove(Long id, String comments);

    ProgressSubmissionResponse advisorReject(Long id, String comments);

    ProgressSubmissionResponse directorApprove(Long id, String comments);

    ProgressSubmissionResponse directorReject(Long id, String comments);
}

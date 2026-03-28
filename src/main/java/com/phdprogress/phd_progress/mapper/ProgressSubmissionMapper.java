package com.phdprogress.phd_progress.mapper;

import com.phdprogress.phd_progress.dto.submission.ProgressSubmissionRequest;
import com.phdprogress.phd_progress.dto.submission.ProgressSubmissionResponse;
import com.phdprogress.phd_progress.entity.ProgressSubmission;
import org.springframework.stereotype.Component;

@Component
public class ProgressSubmissionMapper {

    public ProgressSubmission toEntity(ProgressSubmissionRequest request) {
        ProgressSubmission submission = new ProgressSubmission();
        submission.setTitle(request.getTitle());
        submission.setDescription(request.getDescription());
        submission.setAdvisorId(request.getAdvisorId());
        submission.setDirectorId(request.getDirectorId());
        return submission;
    }

    public void updateEntity(ProgressSubmission submission, ProgressSubmissionRequest request) {
        submission.setTitle(request.getTitle());
        submission.setDescription(request.getDescription());
        submission.setAdvisorId(request.getAdvisorId());
        submission.setDirectorId(request.getDirectorId());
    }

    public ProgressSubmissionResponse toResponse(ProgressSubmission submission) {
        ProgressSubmissionResponse response = new ProgressSubmissionResponse();
        response.setId(submission.getId());
        response.setTitle(submission.getTitle());
        response.setDescription(submission.getDescription());
        response.setStatus(submission.getStatus());
        response.setAdvisorStatus(submission.getAdvisorStatus());
        response.setDirectorStatus(submission.getDirectorStatus());
        response.setAdvisorComments(submission.getAdvisorComments());
        response.setDirectorComments(submission.getDirectorComments());
        response.setFileName(submission.getFileName());
        response.setFileType(submission.getFileType());
        response.setFileSize(submission.getFileSize());
        response.setUploadedAt(submission.getUploadedAt());
        response.setCreatedBy(submission.getCreatedBy());
        response.setAdvisorId(submission.getAdvisorId());
        response.setDirectorId(submission.getDirectorId());
        response.setCreatedAt(submission.getCreatedAt());
        response.setUpdatedAt(submission.getUpdatedAt());
        response.setSubmittedAt(submission.getSubmittedAt());
        response.setAdvisorApprovedAt(submission.getAdvisorApprovedAt());
        response.setAdvisorRejectedAt(submission.getAdvisorRejectedAt());
        response.setDirectorApprovedAt(submission.getDirectorApprovedAt());
        response.setDirectorRejectedAt(submission.getDirectorRejectedAt());
        response.setCompletedAt(submission.getCompletedAt());
        if (submission.getStudent() != null) {
            response.setStudentId(submission.getStudent().getId());
            response.setStudentUsername(submission.getStudent().getUsername());
        }
        return response;
    }
}

package com.phdprogress.phd_progress.dto.submission;

import com.phdprogress.phd_progress.entity.SubmissionStatus;

import java.time.Instant;

public class ProgressSubmissionResponse {

    private Long id;
    private String title;
    private String description;
    private SubmissionStatus status;
    private String advisorStatus;
    private String directorStatus;
    private String advisorComments;
    private String directorComments;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Instant uploadedAt;
    private Long createdBy;
    private Long advisorId;
    private Long directorId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant submittedAt;
    private Instant advisorApprovedAt;
    private Instant advisorRejectedAt;
    private Instant directorApprovedAt;
    private Instant directorRejectedAt;
    private Instant completedAt;
    private Long studentId;
    private String studentUsername;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public String getAdvisorStatus() {
        return advisorStatus;
    }

    public void setAdvisorStatus(String advisorStatus) {
        this.advisorStatus = advisorStatus;
    }

    public String getDirectorStatus() {
        return directorStatus;
    }

    public void setDirectorStatus(String directorStatus) {
        this.directorStatus = directorStatus;
    }

    public String getAdvisorComments() {
        return advisorComments;
    }

    public void setAdvisorComments(String advisorComments) {
        this.advisorComments = advisorComments;
    }

    public String getDirectorComments() {
        return directorComments;
    }

    public void setDirectorComments(String directorComments) {
        this.directorComments = directorComments;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(Long advisorId) {
        this.advisorId = advisorId;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Instant getAdvisorApprovedAt() {
        return advisorApprovedAt;
    }

    public void setAdvisorApprovedAt(Instant advisorApprovedAt) {
        this.advisorApprovedAt = advisorApprovedAt;
    }

    public Instant getAdvisorRejectedAt() {
        return advisorRejectedAt;
    }

    public void setAdvisorRejectedAt(Instant advisorRejectedAt) {
        this.advisorRejectedAt = advisorRejectedAt;
    }

    public Instant getDirectorApprovedAt() {
        return directorApprovedAt;
    }

    public void setDirectorApprovedAt(Instant directorApprovedAt) {
        this.directorApprovedAt = directorApprovedAt;
    }

    public Instant getDirectorRejectedAt() {
        return directorRejectedAt;
    }

    public void setDirectorRejectedAt(Instant directorRejectedAt) {
        this.directorRejectedAt = directorRejectedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }
}

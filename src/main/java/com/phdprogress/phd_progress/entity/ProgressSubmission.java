package com.phdprogress.phd_progress.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "progress_submissions")
@EntityListeners(AuditingEntityListener.class)
public class ProgressSubmission {
    public String getAdvisorStatus() {
        return advisorStatus;
    }

    public String getDirectorStatus() {
        return directorStatus;
    }

    public String getAdvisorComments() {
        return advisorComments;
    }

    public String getDirectorComments() {
        return directorComments;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setDirectorComments(String directorComments) {
        this.directorComments = directorComments;
    }

    public void setAdvisorComments(String advisorComments) {
        this.advisorComments = advisorComments;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public void setDirectorStatus(String directorStatus) {
        this.directorStatus = directorStatus;
    }

    public void setAdvisorStatus(String advisorStatus) {
        this.advisorStatus = advisorStatus;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "submission_status", length = 50)
    private SubmissionStatus status;

    private String advisorStatus;

    private String directorStatus;

    private String advisorComments;

    private String directorComments;

    private String fileName;

    private String filePath;

    private String fileType;

    private Long fileSize;

    private Instant uploadedAt;

    private Long createdBy;

    private Long advisorId;

    private Long directorId;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column
    private Instant updatedAt;

    private Instant advisorApprovedAt;

    private Instant submittedAt;

    private Instant advisorRejectedAt;

    private Instant directorApprovedAt;

    private Instant directorRejectedAt;

    private Instant completedAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    public ProgressSubmission() {
    }

    public ProgressSubmission(String title, String description, SubmissionStatus status, User student) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.student = student;
    }

    public Long getId() {
        return id;
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

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getAdvisorApprovedAt() {
        return advisorApprovedAt;
    }

    public void setAdvisorApprovedAt(Instant advisorApprovedAt) {
        this.advisorApprovedAt = advisorApprovedAt;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
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
}

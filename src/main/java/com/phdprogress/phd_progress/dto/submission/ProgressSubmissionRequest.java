package com.phdprogress.phd_progress.dto.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProgressSubmissionRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String description;

    private Long studentId;

    private Long advisorId;

    private Long directorId;

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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

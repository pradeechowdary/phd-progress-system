package com.phdprogress.phd_progress.dto.submission;

import jakarta.validation.constraints.NotBlank;

public class ApprovalActionRequest {

    @NotBlank
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

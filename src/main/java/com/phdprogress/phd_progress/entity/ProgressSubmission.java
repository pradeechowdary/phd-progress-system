package com.phdprogress.phd_progress.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "progress_submissions")
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

    public void setDirectorComments(String directorComments) {
        this.directorComments = directorComments;
    }

    public void setAdvisorComments(String advisorComments) {
        this.advisorComments = advisorComments;
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

    private String title;

    private String description;

    private String status;

    private String advisorStatus;

    private String directorStatus;

    private String advisorComments;

    private String directorComments;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    public ProgressSubmission() {
    }

    public ProgressSubmission(String title, String description, String status, User student) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
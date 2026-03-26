package com.phdprogress.phd_progress.service;

import com.phdprogress.phd_progress.entity.ProgressSubmission;
import com.phdprogress.phd_progress.repository.ProgressSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressSubmissionService {

    private final ProgressSubmissionRepository repository;

    public ProgressSubmissionService(ProgressSubmissionRepository repository) {
        this.repository = repository;
    }

    public ProgressSubmission createSubmission(ProgressSubmission submission) {
        submission.setStatus("PENDING");
        return repository.save(submission);
    }

    public List<ProgressSubmission> getAllSubmissions() {
        return repository.findAll();
    }

    public ProgressSubmission advisorApprove(Long id, String comments) {

        ProgressSubmission submission =
                repository.findById(id).orElseThrow();

        submission.setAdvisorStatus("APPROVED");
        submission.setAdvisorComments(comments);

        return repository.save(submission);
    }

    public ProgressSubmission directorApprove(Long id, String comments) {

        ProgressSubmission submission =
                repository.findById(id).orElseThrow();

        if (!"APPROVED".equals(submission.getAdvisorStatus())) {
            throw new RuntimeException("Advisor must approve first");
        }

        submission.setDirectorStatus("APPROVED");
        submission.setDirectorComments(comments);
        submission.setStatus("APPROVED");

        return repository.save(submission);
    }

    public ProgressSubmission advisorReject(Long id, String comments) {

        ProgressSubmission submission =
                repository.findById(id).orElseThrow();

        submission.setAdvisorStatus("REJECTED");
        submission.setAdvisorComments(comments);
        submission.setStatus("REJECTED");

        return repository.save(submission);
    }


}
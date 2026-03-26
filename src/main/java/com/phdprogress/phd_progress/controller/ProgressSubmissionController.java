package com.phdprogress.phd_progress.controller;

import com.phdprogress.phd_progress.entity.ProgressSubmission;
import com.phdprogress.phd_progress.service.ProgressSubmissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submissions")
public class ProgressSubmissionController {

    private final ProgressSubmissionService service;

    public ProgressSubmissionController(ProgressSubmissionService service) {
        this.service = service;
    }

    @PostMapping
    public ProgressSubmission createSubmission(@RequestBody ProgressSubmission submission) {
        return service.createSubmission(submission);
    }

    @GetMapping
    public List<ProgressSubmission> getSubmissions() {
        return service.getAllSubmissions();
    }

    @PutMapping("/{id}/advisor-approve")
    public ProgressSubmission advisorApprove(
            @PathVariable Long id,
            @RequestParam String comments) {

        return service.advisorApprove(id, comments);
    }

    @PutMapping("/{id}/director-approve")
    public ProgressSubmission directorApprove(
            @PathVariable Long id,
            @RequestParam String comments) {

        return service.directorApprove(id, comments);
    }

    @PutMapping("/{id}/advisor-reject")
    public ProgressSubmission advisorReject(
            @PathVariable Long id,
            @RequestParam String comments) {

        return service.advisorReject(id, comments);
    }


}
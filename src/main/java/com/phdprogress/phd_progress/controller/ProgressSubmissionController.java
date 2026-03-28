package com.phdprogress.phd_progress.controller;

import com.phdprogress.phd_progress.dto.submission.ApprovalActionRequest;
import com.phdprogress.phd_progress.dto.submission.ProgressSubmissionRequest;
import com.phdprogress.phd_progress.dto.submission.ProgressSubmissionResponse;
import com.phdprogress.phd_progress.dto.submission.SubmissionFileDownload;
import com.phdprogress.phd_progress.entity.SubmissionStatus;
import com.phdprogress.phd_progress.service.ProgressSubmissionService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submissions")
public class ProgressSubmissionController {

    private final ProgressSubmissionService service;

    public ProgressSubmissionController(ProgressSubmissionService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ProgressSubmissionResponse> createSubmission(
            @Valid @RequestBody ProgressSubmissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSubmission(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'ADVISOR', 'DIRECTOR', 'ADMIN')")
    public Page<ProgressSubmissionResponse> getSubmissions(
            @RequestParam(required = false) SubmissionStatus status,
            @RequestParam(required = false) Long studentId,
            Pageable pageable) {
        return service.getAllSubmissions(status, studentId, pageable);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADVISOR', 'DIRECTOR', 'ADMIN')")
    public Page<ProgressSubmissionResponse> getMySubmissions(
            @RequestParam(required = false) SubmissionStatus status,
            Pageable pageable) {
        return service.getMySubmissions(status, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADVISOR', 'DIRECTOR', 'ADMIN')")
    public ProgressSubmissionResponse getSubmission(@PathVariable Long id) {
        return service.getSubmissionById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ProgressSubmissionResponse updateSubmission(
            @PathVariable Long id,
            @Valid @RequestBody ProgressSubmissionRequest request) {
        return service.updateSubmission(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubmission(@PathVariable Long id) {
        service.deleteSubmission(id);
    }

    @PostMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('STUDENT')")
    public ProgressSubmissionResponse uploadSubmissionFile(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) {
        return service.uploadFile(id, file);
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADVISOR', 'DIRECTOR', 'ADMIN')")
    public ResponseEntity<Resource> downloadSubmissionFile(@PathVariable Long id) {
        SubmissionFileDownload download = service.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(download.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(download.getFileName()).build().toString())
                .body(download.getResource());
    }

    @PutMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ProgressSubmissionResponse submit(@PathVariable Long id) {
        return service.submit(id);
    }

    @PutMapping("/{id}/advisor-approve")
    @PreAuthorize("hasAnyRole('ADVISOR', 'ADMIN')")
    public ProgressSubmissionResponse advisorApprove(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalActionRequest request) {

        return service.advisorApprove(id, request.getComments());
    }

    @PutMapping("/{id}/director-approve")
    @PreAuthorize("hasAnyRole('DIRECTOR', 'ADMIN')")
    public ProgressSubmissionResponse directorApprove(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalActionRequest request) {

        return service.directorApprove(id, request.getComments());
    }

    @PutMapping("/{id}/advisor-reject")
    @PreAuthorize("hasAnyRole('ADVISOR', 'ADMIN')")
    public ProgressSubmissionResponse advisorReject(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalActionRequest request) {

        return service.advisorReject(id, request.getComments());
    }

    @PutMapping("/{id}/director-reject")
    @PreAuthorize("hasAnyRole('DIRECTOR', 'ADMIN')")
    public ProgressSubmissionResponse directorReject(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalActionRequest request) {

        return service.directorReject(id, request.getComments());
    }
}

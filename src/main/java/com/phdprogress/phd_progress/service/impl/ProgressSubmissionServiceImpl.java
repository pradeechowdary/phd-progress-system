package com.phdprogress.phd_progress.service.impl;

import com.phdprogress.phd_progress.dto.submission.ProgressSubmissionRequest;
import com.phdprogress.phd_progress.dto.submission.ProgressSubmissionResponse;
import com.phdprogress.phd_progress.dto.submission.SubmissionFileDownload;
import com.phdprogress.phd_progress.entity.ProgressSubmission;
import com.phdprogress.phd_progress.entity.SubmissionStatus;
import com.phdprogress.phd_progress.entity.User;
import com.phdprogress.phd_progress.exception.FileStorageException;
import com.phdprogress.phd_progress.exception.InvalidWorkflowException;
import com.phdprogress.phd_progress.exception.ResourceNotFoundException;
import com.phdprogress.phd_progress.exception.UnauthorizedWorkflowException;
import com.phdprogress.phd_progress.mapper.ProgressSubmissionMapper;
import com.phdprogress.phd_progress.repository.ProgressSubmissionRepository;
import com.phdprogress.phd_progress.repository.UserRepository;
import com.phdprogress.phd_progress.security.SecurityUtils;
import com.phdprogress.phd_progress.service.AuditLogService;
import com.phdprogress.phd_progress.service.EmailService;
import com.phdprogress.phd_progress.service.FileStorageService;
import com.phdprogress.phd_progress.service.ProgressSubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@Transactional
public class ProgressSubmissionServiceImpl implements ProgressSubmissionService {

    private static final Logger log = LoggerFactory.getLogger(ProgressSubmissionServiceImpl.class);

    private final ProgressSubmissionRepository repository;
    private final UserRepository userRepository;
    private final ProgressSubmissionMapper mapper;
    private final AuditLogService auditLogService;
    private final SecurityUtils securityUtils;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;

    public ProgressSubmissionServiceImpl(ProgressSubmissionRepository repository,
                                         UserRepository userRepository,
                                         ProgressSubmissionMapper mapper,
                                         AuditLogService auditLogService,
                                         SecurityUtils securityUtils,
                                         FileStorageService fileStorageService,
                                         EmailService emailService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.auditLogService = auditLogService;
        this.securityUtils = securityUtils;
        this.fileStorageService = fileStorageService;
        this.emailService = emailService;
    }

    @Override
    public ProgressSubmissionResponse createSubmission(ProgressSubmissionRequest request) {
        User actor = securityUtils.getCurrentUser();
        User student = resolveStudent(actor, request.getStudentId());

        ProgressSubmission submission = mapper.toEntity(request);
        submission.setStudent(student);
        submission.setCreatedBy(actor.getId());
        submission.setStatus(SubmissionStatus.DRAFT);
        submission.setAdvisorStatus("PENDING");
        submission.setDirectorStatus("PENDING");

        validateAssignees(submission);

        ProgressSubmission savedSubmission = repository.save(submission);
        auditLogService.log("SUBMISSION_CREATED", "PROGRESS_SUBMISSION", savedSubmission.getId(), actor.getUsername(), "Student created draft submission");
        log.info("Created submission id={} studentId={} status={}", savedSubmission.getId(), student.getId(), savedSubmission.getStatus());
        return mapper.toResponse(savedSubmission);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProgressSubmissionResponse> getAllSubmissions(SubmissionStatus status, Long studentId, Pageable pageable) {
        User actor = securityUtils.getCurrentUser();
        Specification<ProgressSubmission> specification = buildVisibilitySpecification(actor, status, studentId, false);
        return repository.findAll(specification, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProgressSubmissionResponse> getMySubmissions(SubmissionStatus status, Pageable pageable) {
        User actor = securityUtils.getCurrentUser();
        Specification<ProgressSubmission> specification = buildVisibilitySpecification(actor, status, null, true);
        return repository.findAll(specification, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgressSubmissionResponse getSubmissionById(Long id) {
        User actor = securityUtils.getCurrentUser();
        ProgressSubmission submission = findSubmission(id);
        assertCanViewSubmission(actor, submission);
        return mapper.toResponse(submission);
    }

    @Override
    public ProgressSubmissionResponse updateSubmission(Long id, ProgressSubmissionRequest request) {
        User actor = securityUtils.getCurrentUser();
        ProgressSubmission submission = findSubmission(id);
        assertCanEditSubmission(actor, submission);

        User student = resolveStudentForUpdate(actor, request.getStudentId(), submission);
        mapper.updateEntity(submission, request);
        submission.setStudent(student);
        validateAssignees(submission);

        ProgressSubmission savedSubmission = repository.save(submission);
        auditLogService.log("SUBMISSION_UPDATED", "PROGRESS_SUBMISSION", savedSubmission.getId(), actor.getUsername(), "Draft submission updated");
        log.info("Updated submission id={} by={}", savedSubmission.getId(), actor.getUsername());
        return mapper.toResponse(savedSubmission);
    }

    @Override
    public void deleteSubmission(Long id) {
        User actor = securityUtils.getCurrentUser();
        ProgressSubmission submission = findSubmission(id);
        assertCanEditSubmission(actor, submission);
        fileStorageService.deleteFile(submission.getFilePath());
        auditLogService.log("SUBMISSION_DELETED", "PROGRESS_SUBMISSION", submission.getId(), actor.getUsername(), "Submission deleted");
        repository.delete(submission);
        log.info("Deleted submission id={} by={}", id, actor.getUsername());
    }

    @Override
    public ProgressSubmissionResponse uploadFile(Long id, MultipartFile file) {
        User actor = securityUtils.getCurrentUser();
        ProgressSubmission submission = findSubmission(id);
        assertStudentUploadOwnership(actor, submission);

        String previousFilePath = submission.getFilePath();
        FileStorageService.StoredFile storedFile = fileStorageService.storeFile(id, file);
        submission.setFileName(storedFile.getFileName());
        submission.setFilePath(storedFile.getFilePath());
        submission.setFileType(storedFile.getFileType());
        submission.setFileSize(storedFile.getFileSize());
        submission.setUploadedAt(Instant.now());

        if (previousFilePath != null && !previousFilePath.equals(storedFile.getFilePath())) {
            fileStorageService.deleteFile(previousFilePath);
        }

        ProgressSubmission savedSubmission = repository.save(submission);
        auditLogService.log("SUBMISSION_FILE_UPLOADED", "PROGRESS_SUBMISSION", savedSubmission.getId(), actor.getUsername(), "Student uploaded submission file");
        log.info("Uploaded file for submission id={} by={}", savedSubmission.getId(), actor.getUsername());
        return mapper.toResponse(savedSubmission);
    }

    @Override
    @Transactional(readOnly = true)
    public SubmissionFileDownload downloadFile(Long id) {
        User actor = securityUtils.getCurrentUser();
        ProgressSubmission submission = findSubmission(id);
        assertCanViewSubmission(actor, submission);

        if (submission.getFilePath() == null || submission.getFileName() == null) {
            throw new FileStorageException("No file uploaded for this submission");
        }

        return new SubmissionFileDownload(
                fileStorageService.getFile(submission.getFilePath()),
                submission.getFileName(),
                submission.getFileType() != null ? submission.getFileType() : "application/octet-stream"
        );
    }

    @Override
    public ProgressSubmissionResponse submit(Long id) {
        User actor = securityUtils.getCurrentUser();
        ProgressSubmission submission = findSubmission(id);
        assertStudentOwnerOrAdmin(actor, submission);
        transition(submission, SubmissionStatus.SUBMITTED, "Submission must be in DRAFT before it can be submitted");
        submission.setSubmittedAt(Instant.now());

        ProgressSubmission savedSubmission = repository.save(submission);
        auditLogService.log("STUDENT_SUBMITTED", "PROGRESS_SUBMISSION", savedSubmission.getId(), actor.getUsername(), "Student submitted progress");
        notifyAdvisorOnSubmit(savedSubmission, actor);
        log.info("Submitted submission id={} by={}", savedSubmission.getId(), actor.getUsername());
        return mapper.toResponse(savedSubmission);
    }

    @Override
    public ProgressSubmissionResponse advisorApprove(Long id, String comments) {
        User actor = securityUtils.getCurrentUser();
        ProgressSubmission submission = findSubmission(id);
        assertAdvisorOwnership(actor, submission);
        transition(submission, SubmissionStatus.ADVISOR_APPROVED, "Only submitted records can be advisor-approved");
        submission.setAdvisorStatus("APPROVED");
        submission.setAdvisorComments(comments);
        submission.setAdvisorApprovedAt(Instant.now());

        ProgressSubmission savedSubmission = repository.save(submission);
        auditLogService.log("ADVISOR_APPROVED", "PROGRESS_SUBMISSION", savedSubmission.getId(), actor.getUsername(), comments);
        notifyDirectorOnAdvisorApproval(savedSubmission, actor);
        log.info("Advisor approved submission id={} by={}", savedSubmission.getId(), actor.getUsername());
        return mapper.toResponse(savedSubmission);
    }

    @Override
    public ProgressSubmissionResponse advisorReject(Long id, String comments) {
        User actor = securityUtils.getCurrentUser();
        ProgressSubmission submission = findSubmission(id);
        assertAdvisorOwnership(actor, submission);
        transition(submission, SubmissionStatus.ADVISOR_REJECTED, "Only submitted records can be advisor-rejected");
        submission.setAdvisorStatus("REJECTED");
        submission.setAdvisorComments(comments);
        submission.setAdvisorRejectedAt(Instant.now());

        ProgressSubmission savedSubmission = repository.save(submission);
        auditLogService.log("ADVISOR_REJECTED", "PROGRESS_SUBMISSION", savedSubmission.getId(), actor.getUsername(), comments);
        notifyStudentOnAdvisorRejection(savedSubmission, actor);
        log.info("Advisor rejected submission id={} by={}", savedSubmission.getId(), actor.getUsername());
        return mapper.toResponse(savedSubmission);
    }

    @Override
    public ProgressSubmissionResponse directorApprove(Long id, String comments) {
        User actor = securityUtils.getCurrentUser();
        ProgressSubmission submission = findSubmission(id);
        assertDirectorOwnership(actor, submission);
        transition(submission, SubmissionStatus.DIRECTOR_APPROVED, "Director approval requires ADVISOR_APPROVED status");
        submission.setDirectorStatus("APPROVED");
        submission.setDirectorComments(comments);
        submission.setDirectorApprovedAt(Instant.now());

        transition(submission, SubmissionStatus.COMPLETED, "Director-approved submissions can be completed only once");
        submission.setCompletedAt(Instant.now());

        ProgressSubmission savedSubmission = repository.save(submission);
        auditLogService.log("DIRECTOR_APPROVED", "PROGRESS_SUBMISSION", savedSubmission.getId(), actor.getUsername(), comments);
        auditLogService.log("SUBMISSION_COMPLETED", "PROGRESS_SUBMISSION", savedSubmission.getId(), actor.getUsername(), "Workflow completed");
        notifyStudentOnDirectorApproval(savedSubmission, actor);
        log.info("Director approved and completed submission id={} by={}", savedSubmission.getId(), actor.getUsername());
        return mapper.toResponse(savedSubmission);
    }

    @Override
    public ProgressSubmissionResponse directorReject(Long id, String comments) {
        User actor = securityUtils.getCurrentUser();
        ProgressSubmission submission = findSubmission(id);
        assertDirectorOwnership(actor, submission);
        transition(submission, SubmissionStatus.DIRECTOR_REJECTED, "Director rejection requires ADVISOR_APPROVED status");
        submission.setDirectorStatus("REJECTED");
        submission.setDirectorComments(comments);
        submission.setDirectorRejectedAt(Instant.now());

        ProgressSubmission savedSubmission = repository.save(submission);
        auditLogService.log("DIRECTOR_REJECTED", "PROGRESS_SUBMISSION", savedSubmission.getId(), actor.getUsername(), comments);
        notifyStudentOnDirectorRejection(savedSubmission, actor);
        log.info("Director rejected submission id={} by={}", savedSubmission.getId(), actor.getUsername());
        return mapper.toResponse(savedSubmission);
    }

    private ProgressSubmission findSubmission(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found: " + id));
    }

    private User resolveStudent(User actor, Long studentId) {
        if (isAdmin(actor) && studentId != null) {
            User student = findUser(studentId);
            validateStudentRole(student);
            return student;
        }

        if (!isStudent(actor) && !isAdmin(actor)) {
            throw new UnauthorizedWorkflowException("Only students or admins can create submissions");
        }

        if (studentId != null && !actor.getId().equals(studentId) && !isAdmin(actor)) {
            throw new UnauthorizedWorkflowException("Students can only create their own submissions");
        }

        validateStudentRole(actor);
        return actor;
    }

    private User resolveStudentForUpdate(User actor, Long requestedStudentId, ProgressSubmission submission) {
        if (isAdmin(actor) && requestedStudentId != null) {
            User student = findUser(requestedStudentId);
            validateStudentRole(student);
            return student;
        }

        return submission.getStudent();
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    private User findUserIfPresent(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id).orElse(null);
    }

    private void validateStudentRole(User student) {
        if (!"STUDENT".equalsIgnoreCase(student.getRole())) {
            throw new InvalidWorkflowException("Submission owner must have STUDENT role");
        }
    }

    private void validateAssignees(ProgressSubmission submission) {
        if (submission.getAdvisorId() != null) {
            User advisor = findUser(submission.getAdvisorId());
            if (!"ADVISOR".equalsIgnoreCase(advisor.getRole())) {
                throw new InvalidWorkflowException("advisorId must reference an ADVISOR user");
            }
        }

        if (submission.getDirectorId() != null) {
            User director = findUser(submission.getDirectorId());
            if (!"DIRECTOR".equalsIgnoreCase(director.getRole())) {
                throw new InvalidWorkflowException("directorId must reference a DIRECTOR user");
            }
        }
    }

    private void transition(ProgressSubmission submission, SubmissionStatus targetStatus, String message) {
        SubmissionStatus currentStatus = submission.getStatus();
        if (currentStatus == null) {
            throw new InvalidWorkflowException("Submission status is not initialized");
        }

        if (!currentStatus.canTransitionTo(targetStatus)) {
            throw new InvalidWorkflowException(message + ". Current status: " + currentStatus);
        }

        submission.setStatus(targetStatus);
    }

    private void assertCanViewSubmission(User actor, ProgressSubmission submission) {
        if (isAdmin(actor)) {
            return;
        }

        if (isStudent(actor) && submission.getStudent() != null && actor.getId().equals(submission.getStudent().getId())) {
            return;
        }

        if (isAdvisor(actor) && actor.getId().equals(submission.getAdvisorId())) {
            return;
        }

        if (isDirector(actor) && actor.getId().equals(submission.getDirectorId())) {
            return;
        }

        throw new UnauthorizedWorkflowException("You are not authorized to view this submission");
    }

    private void assertCanEditSubmission(User actor, ProgressSubmission submission) {
        if (isAdmin(actor)) {
            return;
        }

        if (!isStudent(actor) || submission.getStudent() == null || !actor.getId().equals(submission.getStudent().getId())) {
            throw new UnauthorizedWorkflowException("Only the owning student can edit this submission");
        }

        if (submission.getStatus() != SubmissionStatus.DRAFT) {
            throw new InvalidWorkflowException("Only DRAFT submissions can be edited");
        }
    }

    private void assertStudentOwnerOrAdmin(User actor, ProgressSubmission submission) {
        if (isAdmin(actor)) {
            return;
        }

        if (!isStudent(actor) || submission.getStudent() == null || !actor.getId().equals(submission.getStudent().getId())) {
            throw new UnauthorizedWorkflowException("Only the owning student can submit this workflow");
        }
    }

    private void assertStudentUploadOwnership(User actor, ProgressSubmission submission) {
        if (!isStudent(actor) || submission.getStudent() == null || !actor.getId().equals(submission.getStudent().getId())) {
            throw new UnauthorizedWorkflowException("Only the owning student can upload a file for this submission");
        }
    }

    private void assertAdvisorOwnership(User actor, ProgressSubmission submission) {
        if (isAdmin(actor)) {
            return;
        }

        if (!isAdvisor(actor)) {
            throw new UnauthorizedWorkflowException("Only advisors can approve or reject at advisor stage");
        }

        if (submission.getAdvisorId() == null || !actor.getId().equals(submission.getAdvisorId())) {
            throw new UnauthorizedWorkflowException("Advisor can only act on assigned submissions");
        }
    }

    private void assertDirectorOwnership(User actor, ProgressSubmission submission) {
        if (isAdmin(actor)) {
            return;
        }

        if (!isDirector(actor)) {
            throw new UnauthorizedWorkflowException("Only directors can approve or reject at director stage");
        }

        if (submission.getDirectorId() == null || !actor.getId().equals(submission.getDirectorId())) {
            throw new UnauthorizedWorkflowException("Director can only act on assigned submissions");
        }
    }

    private Specification<ProgressSubmission> buildVisibilitySpecification(User actor,
                                                                          SubmissionStatus status,
                                                                          Long studentId,
                                                                          boolean myOnly) {
        Specification<ProgressSubmission> specification = Specification.where(hasStatus(status))
                .and(hasStudentId(studentId));

        if (isAdmin(actor) && !myOnly) {
            return specification;
        }

        if (isStudent(actor)) {
            return specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("student").get("id"), actor.getId()));
        }

        if (isAdvisor(actor)) {
            return specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("advisorId"), actor.getId()));
        }

        if (isDirector(actor)) {
            return specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("directorId"), actor.getId()));
        }

        return specification;
    }

    private Specification<ProgressSubmission> hasStatus(SubmissionStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("status"), status);
    }

    private Specification<ProgressSubmission> hasStudentId(Long studentId) {
        return (root, query, criteriaBuilder) ->
                studentId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("student").get("id"), studentId);
    }

    private boolean isAdmin(User user) {
        return "ADMIN".equalsIgnoreCase(user.getRole());
    }

    private boolean isStudent(User user) {
        return "STUDENT".equalsIgnoreCase(user.getRole());
    }

    private boolean isAdvisor(User user) {
        return "ADVISOR".equalsIgnoreCase(user.getRole());
    }

    private boolean isDirector(User user) {
        return "DIRECTOR".equalsIgnoreCase(user.getRole());
    }

    private void notifyAdvisorOnSubmit(ProgressSubmission submission, User actor) {
        User advisor = findUserIfPresent(submission.getAdvisorId());
        if (advisor == null) {
            log.warn("email_notification_skipped eventType=SUBMISSION_SUBMITTED submissionId={} reason=no_assigned_advisor", submission.getId());
            return;
        }
        emailService.notifyAdvisorSubmissionSubmitted(submission, actor, advisor);
    }

    private void notifyDirectorOnAdvisorApproval(ProgressSubmission submission, User actor) {
        User director = findUserIfPresent(submission.getDirectorId());
        if (director == null) {
            log.warn("email_notification_skipped eventType=ADVISOR_APPROVED submissionId={} reason=no_assigned_director", submission.getId());
            return;
        }
        emailService.notifyDirectorAdvisorApproved(submission, actor, director);
    }

    private void notifyStudentOnAdvisorRejection(ProgressSubmission submission, User actor) {
        if (submission.getStudent() == null) {
            log.warn("email_notification_skipped eventType=ADVISOR_REJECTED submissionId={} reason=no_student", submission.getId());
            return;
        }
        emailService.notifyStudentAdvisorRejected(submission, actor, submission.getStudent());
    }

    private void notifyStudentOnDirectorApproval(ProgressSubmission submission, User actor) {
        if (submission.getStudent() == null) {
            log.warn("email_notification_skipped eventType=DIRECTOR_APPROVED submissionId={} reason=no_student", submission.getId());
            return;
        }
        emailService.notifyStudentDirectorApproved(submission, actor, submission.getStudent());
    }

    private void notifyStudentOnDirectorRejection(ProgressSubmission submission, User actor) {
        if (submission.getStudent() == null) {
            log.warn("email_notification_skipped eventType=DIRECTOR_REJECTED submissionId={} reason=no_student", submission.getId());
            return;
        }
        emailService.notifyStudentDirectorRejected(submission, actor, submission.getStudent());
    }
}

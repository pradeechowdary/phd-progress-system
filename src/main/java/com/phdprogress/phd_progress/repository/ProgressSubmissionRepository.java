package com.phdprogress.phd_progress.repository;

import com.phdprogress.phd_progress.entity.ProgressSubmission;
import com.phdprogress.phd_progress.entity.SubmissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProgressSubmissionRepository extends JpaRepository<ProgressSubmission, Long>, JpaSpecificationExecutor<ProgressSubmission> {

    Page<ProgressSubmission> findByStudentId(Long studentId, Pageable pageable);

    Page<ProgressSubmission> findByStudentIdAndStatus(Long studentId, SubmissionStatus status, Pageable pageable);

    Page<ProgressSubmission> findByAdvisorId(Long advisorId, Pageable pageable);

    Page<ProgressSubmission> findByDirectorId(Long directorId, Pageable pageable);
}

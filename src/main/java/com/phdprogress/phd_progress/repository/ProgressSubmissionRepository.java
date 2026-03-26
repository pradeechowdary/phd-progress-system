package com.phdprogress.phd_progress.repository;

import com.phdprogress.phd_progress.entity.ProgressSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressSubmissionRepository extends JpaRepository<ProgressSubmission, Long> {

}
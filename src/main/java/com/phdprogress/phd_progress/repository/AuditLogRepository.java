package com.phdprogress.phd_progress.repository;

import com.phdprogress.phd_progress.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}

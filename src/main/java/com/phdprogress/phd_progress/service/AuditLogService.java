package com.phdprogress.phd_progress.service;

public interface AuditLogService {

    void log(String action, String entityType, Long entityId, String actor, String details);
}

package com.phdprogress.phd_progress.service.impl;

import com.phdprogress.phd_progress.entity.AuditLog;
import com.phdprogress.phd_progress.repository.AuditLogRepository;
import com.phdprogress.phd_progress.service.AuditLogService;
import org.springframework.stereotype.Service;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void log(String action, String entityType, Long entityId, String actor, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setActor(actor);
        auditLog.setDetails(details);
        auditLogRepository.save(auditLog);
    }
}

package com.crm.audit.service;

import com.crm.audit.entity.AuditLog;
import com.crm.audit.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AuditLog record(String tenantId, Long actorUserId, String action, String entityType,
                            String entityId, String details) {
        AuditLog log = new AuditLog();
        log.setTenantId(tenantId);
        log.setActorUserId(actorUserId);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        return repository.save(log);
    }

    public List<AuditLog> history(String tenantId) {
        return repository.findAllByTenantIdOrderByOccurredAtDesc(tenantId);
    }
}

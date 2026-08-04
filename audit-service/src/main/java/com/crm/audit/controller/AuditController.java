package com.crm.audit.controller;

import com.crm.audit.dto.AuditDtos.*;
import com.crm.audit.entity.AuditLog;
import com.crm.audit.service.AuditService;
import com.crm.common.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService service;

    public AuditController(AuditService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AuditLogResponse> record(@Valid @RequestBody RecordRequest req) {
        var user = CurrentUser.get();
        AuditLog log = service.record(user.tenantId(), user.userId(), req.action(),
                req.entityType(), req.entityId(), req.details());
        return ResponseEntity.ok(toResponse(log));
    }

    @GetMapping
    public List<AuditLogResponse> history() {
        return service.history(CurrentUser.get().tenantId()).stream().map(this::toResponse).toList();
    }

    private AuditLogResponse toResponse(AuditLog log) {
        return new AuditLogResponse(log.getId(), log.getActorUserId(), log.getAction(),
                log.getEntityType(), log.getEntityId(), log.getDetails(), log.getOccurredAt().toString());
    }
}

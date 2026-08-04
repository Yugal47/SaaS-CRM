package com.crm.audit.dto;

import jakarta.validation.constraints.NotBlank;

public class AuditDtos {

    public record RecordRequest(
            @NotBlank String action,
            @NotBlank String entityType,
            String entityId,
            String details
    ) {}

    public record AuditLogResponse(
            Long id,
            Long actorUserId,
            String action,
            String entityType,
            String entityId,
            String details,
            String occurredAt
    ) {}
}

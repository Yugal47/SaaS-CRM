package com.crm.audit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    private Long actorUserId;

    @Column(nullable = false)
    private String action; // e.g. CUSTOMER_CREATED, DEAL_STAGE_CHANGED, USER_INVITED

    @Column(nullable = false)
    private String entityType; // e.g. Customer, Lead, Deal

    private String entityId;

    @Column(length = 2000)
    private String details;

    @Column(nullable = false)
    private Instant occurredAt = Instant.now();
}

package com.crm.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "deals")
@Getter
@Setter
public class Deal extends TenantScopedEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private BigDecimal amount;

    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stage stage = Stage.PROSPECTING;

    private Long ownerUserId;

    public enum Stage {
        PROSPECTING, QUALIFICATION, PROPOSAL, NEGOTIATION, WON, LOST
    }
}

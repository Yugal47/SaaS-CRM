package com.crm.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "leads")
@Getter
@Setter
public class Lead extends TenantScopedEntity {

    @Column(nullable = false)
    private String name;

    private String email;
    private String phone;
    private String source;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.NEW;

    private Long assignedToUserId;

    public enum Status {
        NEW, CONTACTED, QUALIFIED, CONVERTED, LOST
    }
}

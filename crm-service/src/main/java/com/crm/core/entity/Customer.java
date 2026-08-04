package com.crm.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer extends TenantScopedEntity {

    @Column(nullable = false)
    private String name;

    private String email;
    private String phone;
    private String company;

    @Column(length = 2000)
    private String notes;
}

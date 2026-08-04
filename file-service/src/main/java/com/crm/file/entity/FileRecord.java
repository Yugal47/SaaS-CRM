package com.crm.file.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "file_records")
@Getter
@Setter
public class FileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String storedPath;

    @Column(nullable = false)
    private long sizeBytes;

    @Column(nullable = false)
    private String contentType;

    private Long uploadedByUserId;

    @Column(nullable = false)
    private Instant uploadedAt = Instant.now();
}

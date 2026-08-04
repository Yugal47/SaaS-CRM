package com.crm.file.repository;

import com.crm.file.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {
    List<FileRecord> findAllByTenantId(String tenantId);
    Optional<FileRecord> findByIdAndTenantId(Long id, String tenantId);
}

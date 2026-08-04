package com.crm.file.service;

import com.crm.common.exception.ResourceNotFoundException;
import com.crm.file.entity.FileRecord;
import com.crm.file.repository.FileRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final FileRecordRepository repository;
    private final Path rootDir;

    public FileStorageService(FileRecordRepository repository, @Value("${file.storage-path}") String storagePath) {
        this.repository = repository;
        this.rootDir = Path.of(storagePath);
        try {
            Files.createDirectories(rootDir);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create file storage directory: " + storagePath, e);
        }
    }

    @Transactional
    public FileRecord upload(String tenantId, Long uploaderUserId, MultipartFile file) {
        try {
            Path tenantDir = rootDir.resolve(tenantId);
            Files.createDirectories(tenantDir);

            String storedName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path target = tenantDir.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            FileRecord record = new FileRecord();
            record.setTenantId(tenantId);
            record.setOriginalFilename(file.getOriginalFilename());
            record.setStoredPath(target.toString());
            record.setSizeBytes(file.getSize());
            record.setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
            record.setUploadedByUserId(uploaderUserId);

            return repository.save(record);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file: " + e.getMessage(), e);
        }
    }

    public List<FileRecord> list(String tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    public FileRecord getMetadata(String tenantId, Long id) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found: " + id));
    }

    public InputStream download(String tenantId, Long id) {
        FileRecord record = getMetadata(tenantId, id);
        try {
            return Files.newInputStream(Path.of(record.getStoredPath()));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read file: " + e.getMessage(), e);
        }
    }
}

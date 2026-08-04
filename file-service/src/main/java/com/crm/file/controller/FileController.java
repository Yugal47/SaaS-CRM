package com.crm.file.controller;

import com.crm.common.security.CurrentUser;
import com.crm.file.entity.FileRecord;
import com.crm.file.service.FileStorageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService service;

    public FileController(FileStorageService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileRecord> upload(@RequestParam("file") MultipartFile file) {
        var user = CurrentUser.get();
        FileRecord record = service.upload(user.tenantId(), user.userId(), file);
        return ResponseEntity.ok(record);
    }

    @GetMapping
    public List<FileRecord> list() {
        return service.list(CurrentUser.get().tenantId());
    }

    @GetMapping("/{id}")
    public FileRecord metadata(@PathVariable Long id) {
        return service.getMetadata(CurrentUser.get().tenantId(), id);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable Long id) {
        var user = CurrentUser.get();
        FileRecord record = service.getMetadata(user.tenantId(), id);
        InputStreamResource resource = new InputStreamResource(service.download(user.tenantId(), id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + record.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType(record.getContentType()))
                .body(resource);
    }
}

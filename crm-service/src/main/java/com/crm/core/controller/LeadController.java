package com.crm.core.controller;

import com.crm.common.security.CurrentUser;
import com.crm.core.dto.LeadDtos.*;
import com.crm.core.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadService service;

    public LeadController(LeadService service) {
        this.service = service;
    }

    @GetMapping
    public List<LeadResponse> list() {
        return service.listAll(CurrentUser.get().tenantId());
    }

    @GetMapping("/{id}")
    public LeadResponse get(@PathVariable Long id) {
        return service.getOne(CurrentUser.get().tenantId(), id);
    }

    @PostMapping
    public ResponseEntity<LeadResponse> create(@Valid @RequestBody LeadRequest req) {
        return ResponseEntity.ok(service.create(CurrentUser.get().tenantId(), req));
    }

    @PutMapping("/{id}")
    public LeadResponse update(@PathVariable Long id, @Valid @RequestBody LeadRequest req) {
        return service.update(CurrentUser.get().tenantId(), id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(CurrentUser.get().tenantId(), id);
        return ResponseEntity.noContent().build();
    }
}

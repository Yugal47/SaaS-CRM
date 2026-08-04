package com.crm.core.controller;

import com.crm.common.security.CurrentUser;
import com.crm.core.dto.DealDtos.*;
import com.crm.core.service.DealService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealService service;

    public DealController(DealService service) {
        this.service = service;
    }

    @GetMapping
    public List<DealResponse> list() {
        return service.listAll(CurrentUser.get().tenantId());
    }

    @GetMapping("/{id}")
    public DealResponse get(@PathVariable Long id) {
        return service.getOne(CurrentUser.get().tenantId(), id);
    }

    @PostMapping
    public ResponseEntity<DealResponse> create(@Valid @RequestBody DealRequest req) {
        return ResponseEntity.ok(service.create(CurrentUser.get().tenantId(), req));
    }

    @PutMapping("/{id}")
    public DealResponse update(@PathVariable Long id, @Valid @RequestBody DealRequest req) {
        return service.update(CurrentUser.get().tenantId(), id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(CurrentUser.get().tenantId(), id);
        return ResponseEntity.noContent().build();
    }
}

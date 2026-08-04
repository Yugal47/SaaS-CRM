package com.crm.core.controller;

import com.crm.common.security.CurrentUser;
import com.crm.core.dto.CustomerDtos.*;
import com.crm.core.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public List<CustomerResponse> list() {
        return service.listAll(CurrentUser.get().tenantId());
    }

    @GetMapping("/{id}")
    public CustomerResponse get(@PathVariable Long id) {
        return service.getOne(CurrentUser.get().tenantId(), id);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest req) {
        return ResponseEntity.ok(service.create(CurrentUser.get().tenantId(), req));
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable Long id, @Valid @RequestBody CustomerRequest req) {
        return service.update(CurrentUser.get().tenantId(), id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(CurrentUser.get().tenantId(), id);
        return ResponseEntity.noContent().build();
    }
}

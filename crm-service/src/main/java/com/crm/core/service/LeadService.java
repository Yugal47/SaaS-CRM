package com.crm.core.service;

import com.crm.common.exception.ResourceNotFoundException;
import com.crm.core.dto.LeadDtos.*;
import com.crm.core.entity.Lead;
import com.crm.core.repository.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class LeadService {

    private final LeadRepository repository;

    public LeadService(LeadRepository repository) {
        this.repository = repository;
    }

    public List<LeadResponse> listAll(String tenantId) {
        return repository.findAllByTenantId(tenantId).stream().map(this::toResponse).toList();
    }

    public LeadResponse getOne(String tenantId, Long id) {
        return toResponse(findOrThrow(tenantId, id));
    }

    @Transactional
    public LeadResponse create(String tenantId, LeadRequest req) {
        Lead l = new Lead();
        l.setTenantId(tenantId);
        applyRequest(l, req);
        return toResponse(repository.save(l));
    }

    @Transactional
    public LeadResponse update(String tenantId, Long id, LeadRequest req) {
        Lead l = findOrThrow(tenantId, id);
        applyRequest(l, req);
        l.setUpdatedAt(Instant.now());
        return toResponse(repository.save(l));
    }

    @Transactional
    public void delete(String tenantId, Long id) {
        findOrThrow(tenantId, id);
        repository.deleteByIdAndTenantId(id, tenantId);
    }

    private Lead findOrThrow(String tenantId, Long id) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + id));
    }

    private void applyRequest(Lead l, LeadRequest req) {
        l.setName(req.name());
        l.setEmail(req.email());
        l.setPhone(req.phone());
        l.setSource(req.source());
        if (req.status() != null) {
            l.setStatus(Lead.Status.valueOf(req.status().toUpperCase()));
        }
        l.setAssignedToUserId(req.assignedToUserId());
    }

    private LeadResponse toResponse(Lead l) {
        return new LeadResponse(l.getId(), l.getName(), l.getEmail(), l.getPhone(),
                l.getSource(), l.getStatus().name(), l.getAssignedToUserId());
    }
}

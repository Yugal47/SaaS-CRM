package com.crm.core.service;

import com.crm.common.exception.ResourceNotFoundException;
import com.crm.core.dto.DealDtos.*;
import com.crm.core.entity.Deal;
import com.crm.core.repository.DealRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class DealService {

    private final DealRepository repository;

    public DealService(DealRepository repository) {
        this.repository = repository;
    }

    public List<DealResponse> listAll(String tenantId) {
        return repository.findAllByTenantId(tenantId).stream().map(this::toResponse).toList();
    }

    public DealResponse getOne(String tenantId, Long id) {
        return toResponse(findOrThrow(tenantId, id));
    }

    @Transactional
    public DealResponse create(String tenantId, DealRequest req) {
        Deal d = new Deal();
        d.setTenantId(tenantId);
        applyRequest(d, req);
        return toResponse(repository.save(d));
    }

    @Transactional
    public DealResponse update(String tenantId, Long id, DealRequest req) {
        Deal d = findOrThrow(tenantId, id);
        applyRequest(d, req);
        d.setUpdatedAt(Instant.now());
        return toResponse(repository.save(d));
    }

    @Transactional
    public void delete(String tenantId, Long id) {
        findOrThrow(tenantId, id);
        repository.deleteByIdAndTenantId(id, tenantId);
    }

    private Deal findOrThrow(String tenantId, Long id) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found: " + id));
    }

    private void applyRequest(Deal d, DealRequest req) {
        d.setTitle(req.title());
        d.setAmount(req.amount());
        d.setCustomerId(req.customerId());
        if (req.stage() != null) {
            d.setStage(Deal.Stage.valueOf(req.stage().toUpperCase()));
        }
        d.setOwnerUserId(req.ownerUserId());
    }

    private DealResponse toResponse(Deal d) {
        return new DealResponse(d.getId(), d.getTitle(), d.getAmount(), d.getCustomerId(),
                d.getStage().name(), d.getOwnerUserId());
    }
}

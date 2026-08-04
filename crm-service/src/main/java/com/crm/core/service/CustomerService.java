package com.crm.core.service;

import com.crm.common.exception.ResourceNotFoundException;
import com.crm.core.dto.CustomerDtos.*;
import com.crm.core.entity.Customer;
import com.crm.core.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<CustomerResponse> listAll(String tenantId) {
        return repository.findAllByTenantId(tenantId).stream().map(this::toResponse).toList();
    }

    public CustomerResponse getOne(String tenantId, Long id) {
        return toResponse(findOrThrow(tenantId, id));
    }

    @Transactional
    public CustomerResponse create(String tenantId, CustomerRequest req) {
        Customer c = new Customer();
        c.setTenantId(tenantId);
        applyRequest(c, req);
        return toResponse(repository.save(c));
    }

    @Transactional
    public CustomerResponse update(String tenantId, Long id, CustomerRequest req) {
        Customer c = findOrThrow(tenantId, id);
        applyRequest(c, req);
        c.setUpdatedAt(Instant.now());
        return toResponse(repository.save(c));
    }

    @Transactional
    public void delete(String tenantId, Long id) {
        findOrThrow(tenantId, id);
        repository.deleteByIdAndTenantId(id, tenantId);
    }

    private Customer findOrThrow(String tenantId, Long id) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private void applyRequest(Customer c, CustomerRequest req) {
        c.setName(req.name());
        c.setEmail(req.email());
        c.setPhone(req.phone());
        c.setCompany(req.company());
        c.setNotes(req.notes());
    }

    private CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getCompany(), c.getNotes());
    }
}

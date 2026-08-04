package com.crm.core.repository;

import com.crm.core.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long> {
    List<Deal> findAllByTenantId(String tenantId);
    Optional<Deal> findByIdAndTenantId(Long id, String tenantId);
    void deleteByIdAndTenantId(Long id, String tenantId);
}

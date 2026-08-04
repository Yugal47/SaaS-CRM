package com.crm.core.repository;

import com.crm.core.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByTenantId(String tenantId);
    Optional<Customer> findByIdAndTenantId(Long id, String tenantId);
    void deleteByIdAndTenantId(Long id, String tenantId);
}

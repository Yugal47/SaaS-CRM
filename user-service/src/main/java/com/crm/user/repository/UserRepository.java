package com.crm.user.repository;

import com.crm.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTenantIdAndEmail(String tenantId, String email);
    boolean existsByTenantIdAndEmail(String tenantId, String email);
    List<User> findAllByTenantId(String tenantId);
}

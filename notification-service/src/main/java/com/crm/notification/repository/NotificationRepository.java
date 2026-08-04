package com.crm.notification.repository;

import com.crm.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByTenantIdOrderBySentAtDesc(String tenantId);
}

package com.crm.notification.service;

import com.crm.notification.entity.Notification;
import com.crm.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public Notification send(String tenantId, Notification.Channel channel, String recipient, String message) {
        // Stub: logs instead of actually dispatching. Swap this method's body for an
        // SES/SNS SDK call when wiring into AWS - the rest of the service (persistence,
        // API contract, tenant scoping) doesn't change.
        log.info("[{}] Sending {} to {}: {}", tenantId, channel, recipient, message);

        Notification n = new Notification();
        n.setTenantId(tenantId);
        n.setChannel(channel);
        n.setRecipient(recipient);
        n.setMessage(message);
        n.setStatus(Notification.Status.SENT);
        return repository.save(n);
    }

    public List<Notification> history(String tenantId) {
        return repository.findAllByTenantIdOrderBySentAtDesc(tenantId);
    }
}

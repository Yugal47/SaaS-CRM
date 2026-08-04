package com.crm.notification.controller;

import com.crm.common.security.CurrentUser;
import com.crm.notification.dto.NotificationDtos.*;
import com.crm.notification.entity.Notification;
import com.crm.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> send(@Valid @RequestBody SendRequest req) {
        Notification n = service.send(
                CurrentUser.get().tenantId(),
                Notification.Channel.valueOf(req.channel().toUpperCase()),
                req.recipient(),
                req.message()
        );
        return ResponseEntity.ok(toResponse(n));
    }

    @GetMapping
    public List<NotificationResponse> history() {
        return service.history(CurrentUser.get().tenantId()).stream().map(this::toResponse).toList();
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(n.getId(), n.getChannel().name(), n.getRecipient(),
                n.getMessage(), n.getStatus().name(), n.getSentAt().toString());
    }
}

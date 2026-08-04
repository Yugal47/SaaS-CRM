package com.crm.notification.dto;

import jakarta.validation.constraints.NotBlank;

public class NotificationDtos {

    public record SendRequest(
            @NotBlank String channel, // EMAIL or SMS
            @NotBlank String recipient,
            @NotBlank String message
    ) {}

    public record NotificationResponse(
            Long id,
            String channel,
            String recipient,
            String message,
            String status,
            String sentAt
    ) {}
}

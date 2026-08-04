package com.crm.core.dto;

import jakarta.validation.constraints.NotBlank;

public class LeadDtos {
    public record LeadRequest(@NotBlank String name, String email, String phone, String source, String status, Long assignedToUserId) {}
    public record LeadResponse(Long id, String name, String email, String phone, String source, String status, Long assignedToUserId) {}
}

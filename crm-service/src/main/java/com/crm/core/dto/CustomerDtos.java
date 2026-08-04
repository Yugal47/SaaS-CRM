package com.crm.core.dto;

import jakarta.validation.constraints.NotBlank;

public class CustomerDtos {
    public record CustomerRequest(@NotBlank String name, String email, String phone, String company, String notes) {}
    public record CustomerResponse(Long id, String name, String email, String phone, String company, String notes) {}
}

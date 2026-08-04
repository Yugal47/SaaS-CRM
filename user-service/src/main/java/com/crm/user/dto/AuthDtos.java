package com.crm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public record RegisterTenantRequest(
            @NotBlank String organizationName,
            @NotBlank @Size(min = 2, max = 40) String slug,
            @NotBlank String adminName,
            @Email @NotBlank String adminEmail,
            @NotBlank @Size(min = 8) String password
    ) {}

    public record LoginRequest(
            @NotBlank String tenantSlug,
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}

    public record InviteUserRequest(
            @NotBlank String name,
            @Email @NotBlank String email,
            @NotBlank String password,
            @NotBlank String role
    ) {}

    public record AuthResponse(
            String token,
            String tenantId,
            String tenantSlug,
            String email,
            String role
    ) {}

    public record UserSummary(Long id, String name, String email, String role) {}
}

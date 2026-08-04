package com.crm.common.security;

public record AuthenticatedUser(Long userId, String tenantId, String email, String role) {
}

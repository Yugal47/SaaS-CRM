package com.crm.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DealDtos {
    public record DealRequest(@NotBlank String title, @NotNull BigDecimal amount, Long customerId, String stage, Long ownerUserId) {}
    public record DealResponse(Long id, String title, BigDecimal amount, Long customerId, String stage, Long ownerUserId) {}
}

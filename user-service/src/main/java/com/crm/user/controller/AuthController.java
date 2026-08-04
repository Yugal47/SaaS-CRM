package com.crm.user.controller;

import com.crm.common.security.CurrentUser;
import com.crm.user.dto.AuthDtos.*;
import com.crm.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterTenantRequest req) {
        return ResponseEntity.ok(authService.registerTenant(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/users")
    public ResponseEntity<Void> inviteUser(@Valid @RequestBody InviteUserRequest req) {
        authService.inviteUser(CurrentUser.get(), req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public List<UserSummary> listUsers() {
        return authService.listUsers(CurrentUser.get().tenantId());
    }
}

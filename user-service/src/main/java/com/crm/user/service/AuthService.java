package com.crm.user.service;

import com.crm.common.security.AuthenticatedUser;
import com.crm.common.security.JwtUtil;
import com.crm.user.dto.AuthDtos.*;
import com.crm.user.entity.Tenant;
import com.crm.user.entity.User;
import com.crm.user.repository.TenantRepository;
import com.crm.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(TenantRepository tenantRepository, UserRepository userRepository,
                        PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse registerTenant(RegisterTenantRequest req) {
        String slug = req.slug().trim().toLowerCase();

        if (tenantRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Organization slug already taken: " + slug);
        }

        Tenant tenant = new Tenant();
        tenant.setName(req.organizationName());
        tenant.setSlug(slug);
        tenant = tenantRepository.save(tenant);

        String tenantId = String.valueOf(tenant.getId());

        User admin = new User();
        admin.setTenantId(tenantId);
        admin.setName(req.adminName());
        admin.setEmail(req.adminEmail().trim().toLowerCase());
        admin.setPasswordHash(passwordEncoder.encode(req.password()));
        admin.setRole(User.Role.ADMIN);
        admin = userRepository.save(admin);

        String token = jwtUtil.generateToken(admin.getId(), tenantId, admin.getEmail(), admin.getRole().name());
        return new AuthResponse(token, tenantId, tenant.getSlug(), admin.getEmail(), admin.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        Tenant tenant = tenantRepository.findBySlug(req.tenantSlug().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid organization, email, or password"));

        String tenantId = String.valueOf(tenant.getId());

        User user = userRepository.findByTenantIdAndEmail(tenantId, req.email().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid organization, email, or password"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid organization, email, or password");
        }

        String token = jwtUtil.generateToken(user.getId(), tenantId, user.getEmail(), user.getRole().name());
        return new AuthResponse(token, tenantId, tenant.getSlug(), user.getEmail(), user.getRole().name());
    }

    @Transactional
    public void inviteUser(AuthenticatedUser actingUser, InviteUserRequest req) {
        if (!"ADMIN".equals(actingUser.role())) {
            throw new IllegalArgumentException("Only an ADMIN can add users");
        }
        if (userRepository.existsByTenantIdAndEmail(actingUser.tenantId(), req.email().trim().toLowerCase())) {
            throw new IllegalArgumentException("A user with that email already exists in this organization");
        }

        User user = new User();
        user.setTenantId(actingUser.tenantId());
        user.setName(req.name());
        user.setEmail(req.email().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setRole(User.Role.valueOf(req.role().toUpperCase()));
        userRepository.save(user);
    }

    public List<UserSummary> listUsers(String tenantId) {
        return userRepository.findAllByTenantId(tenantId).stream()
                .map(u -> new UserSummary(u.getId(), u.getName(), u.getEmail(), u.getRole().name()))
                .toList();
    }
}

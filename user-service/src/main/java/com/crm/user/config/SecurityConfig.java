package com.crm.user.config;

import com.crm.common.security.BaseSecurityConfig;
import com.crm.common.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig extends BaseSecurityConfig {

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        super(jwtAuthFilter);
    }

    @Override
    protected String[] publicPaths() {
        return new String[]{"/actuator/health", "/api/auth/register", "/api/auth/login"};
    }

    @Bean
    @Override
    public PasswordEncoder passwordEncoder() {
        return super.passwordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return buildFilterChain(http);
    }
}

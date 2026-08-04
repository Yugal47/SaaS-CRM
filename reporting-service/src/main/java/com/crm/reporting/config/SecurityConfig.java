package com.crm.reporting.config;

import com.crm.common.security.BaseSecurityConfig;
import com.crm.common.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig extends BaseSecurityConfig {

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        super(jwtAuthFilter);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return buildFilterChain(http);
    }
}

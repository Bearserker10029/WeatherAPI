package com.example.weatherapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Endpoints del laboratorio de clima requieren autenticación
                        .requestMatchers("/clima/**").authenticated()
                        // Health/info libre si los necesitas
                        .anyRequest().permitAll()
                )
                // Activamos HTTP Basic: el cliente envía "Authorization: Basic base64(user:pass)"
                .httpBasic(basic -> {});

        return http.build();
    }

    /**
     * Bean de usuarios en memoria. Para el laboratorio basta con un usuario único.
     * En producción esto se reemplaza por un UserDetailsService que consulte una tabla users.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails usuario = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(usuario);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

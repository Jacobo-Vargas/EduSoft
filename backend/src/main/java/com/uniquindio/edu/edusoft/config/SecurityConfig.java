package com.uniquindio.edu.edusoft.config;


import com.uniquindio.edu.edusoft.config.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api-public/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/createUser").permitAll()
                        .requestMatchers("/users/sendCodeConfirmation/**").permitAll()
                        .requestMatchers("/users/verifyAccountEmailCode/**").permitAll()
                        .requestMatchers("/api/users/verify/**").permitAll()
                        .requestMatchers("/users/sendCodeEmail/**").permitAll()
                        .requestMatchers("/api/course/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()
                        .requestMatchers("/api/currentStatuses/**").permitAll()
                        .requestMatchers("/api/modules/**").permitAll()
                        .requestMatchers("/api/lessons/**").permitAll()
                        .requestMatchers("/api/contents/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        var configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE, OPTIONS
        configuration.addAllowedHeader("*");
        configuration.setAllowedOriginPatterns( List.of(
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));
        configuration.setAllowedHeaders(List.of("*")); // o enumera: "Authorization","Content-Type",...
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);       // si usas cookies o envías credenciales
        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedHandler() {
        return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
        };
    }
}

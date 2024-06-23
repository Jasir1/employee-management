package com.xrontech.web.domain.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xrontech.web.config.CustomErrorResponse;
import com.xrontech.web.domain.security.entity.UserRole;
import com.xrontech.web.domain.security.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private static final String adminPath = "/admin/**";
    private static final String userPath = "/user/**";
    private static final String attendancePath = "/attendance/**";
    private static final String departmentPath = "/department/**";
    private static final String jobRolePath = "/job-role/**";

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headerConfig -> {
            headerConfig.xssProtection(xXssConfig -> xXssConfig.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED));
            headerConfig.contentSecurityPolicy(contentSecurityPolicyConfig -> contentSecurityPolicyConfig.policyDirectives("default-src 'self'"));
        });
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/token/refresh/{refresh-token}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/forgot-password/{email}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/reset-password/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/reset-password/{id}").permitAll()

                        .requestMatchers(HttpMethod.GET, adminPath).hasRole(String.valueOf(UserRole.ADMIN))
                        .requestMatchers(HttpMethod.POST, adminPath).hasRole(String.valueOf(UserRole.ADMIN))
                        .requestMatchers(HttpMethod.PUT, adminPath).hasRole(String.valueOf(UserRole.ADMIN))
                        .requestMatchers(HttpMethod.DELETE, adminPath).hasRole(String.valueOf(UserRole.ADMIN))

                        .requestMatchers(HttpMethod.GET, userPath).hasAnyRole(String.valueOf(UserRole.ADMIN),String.valueOf(UserRole.USER))
                        .requestMatchers(HttpMethod.POST, userPath).hasAnyRole(String.valueOf(UserRole.ADMIN),String.valueOf(UserRole.USER))
                        .requestMatchers(HttpMethod.PUT, userPath).hasAnyRole(String.valueOf(UserRole.ADMIN),String.valueOf(UserRole.USER))
                        .requestMatchers(HttpMethod.DELETE, userPath).hasAnyRole(String.valueOf(UserRole.ADMIN),String.valueOf(UserRole.USER))

                        .requestMatchers(HttpMethod.GET, attendancePath).hasAnyRole(String.valueOf(UserRole.ADMIN),String.valueOf(UserRole.USER))
                        .requestMatchers(HttpMethod.POST, attendancePath).hasRole(String.valueOf(UserRole.ADMIN))
                        .requestMatchers(HttpMethod.PUT, attendancePath).hasRole(String.valueOf(UserRole.ADMIN))
                        .requestMatchers(HttpMethod.DELETE, attendancePath).hasRole(String.valueOf(UserRole.ADMIN))

                        .requestMatchers(HttpMethod.GET, departmentPath).hasAnyRole(String.valueOf(UserRole.ADMIN),String.valueOf(UserRole.USER))
                        .requestMatchers(HttpMethod.POST, departmentPath).hasRole(String.valueOf(UserRole.ADMIN))
                        .requestMatchers(HttpMethod.PUT, departmentPath).hasRole(String.valueOf(UserRole.ADMIN))
                        .requestMatchers(HttpMethod.DELETE, departmentPath).hasRole(String.valueOf(UserRole.ADMIN))

                        .requestMatchers(HttpMethod.GET, jobRolePath).hasAnyRole(String.valueOf(UserRole.ADMIN),String.valueOf(UserRole.USER))
                        .requestMatchers(HttpMethod.POST, jobRolePath).hasRole(String.valueOf(UserRole.ADMIN))
                        .requestMatchers(HttpMethod.PUT, jobRolePath).hasRole(String.valueOf(UserRole.ADMIN))
                        .requestMatchers(HttpMethod.DELETE, jobRolePath).hasRole(String.valueOf(UserRole.ADMIN))


                        .anyRequest().authenticated()
                )

                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(accessDeniedHandler())
                );

        http.addFilterAfter(jwtTokenFilter, CorsFilter.class);
        return http.build();
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            CustomErrorResponse errorResponse = new CustomErrorResponse(
                    HttpStatus.FORBIDDEN.value(),
                    "ApplicationCustomException",
                    "Unauthorized access"
            );
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

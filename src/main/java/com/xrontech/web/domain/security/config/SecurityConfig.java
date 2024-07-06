package com.xrontech.web.domain.security.config;

import com.xrontech.web.domain.security.entity.UserRole;
import com.xrontech.web.domain.security.filter.JwtTokenFilter;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private static final String DEPARTMENT_URL = "/department/**";
    private static final String DOCUMENT_URL = "/document/**";
    private static final String EMPLOYEE_URL = "/employee/**";
    private static final String JOB_ROLE_URL = "/job-role/**";
    private static final String PAYROLL_URL = "/payroll/**";

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

                .requestMatchers(HttpMethod.GET, "/auth/reset-password/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/token/refresh/{refresh-token}").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/forgot-password/{email}").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/reset-password/{id}").permitAll()

                .requestMatchers(HttpMethod.PUT, "/auth/reset-password").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))

                .requestMatchers(HttpMethod.GET, "/user/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, "/user/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))

                .requestMatchers(HttpMethod.GET, "/attendance/get/**").hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.GET, "/attendance/get-own/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.GET, "/attendance/is-check-in").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, "/attendance/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, "/attendance/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))

                .requestMatchers(HttpMethod.GET, "/complaint/get/**").hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.GET, "/complaint/get-own/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, "/complaint/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, "/complaint/**").hasRole(String.valueOf(UserRole.ADMIN))

                .requestMatchers(HttpMethod.GET, DEPARTMENT_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.POST, DEPARTMENT_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.PUT, DEPARTMENT_URL).hasRole(String.valueOf(UserRole.ADMIN))

                .requestMatchers(HttpMethod.GET, "/document/get/**").hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.GET, "/document/get-own/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, DOCUMENT_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.PUT, DOCUMENT_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.DELETE, DOCUMENT_URL).hasRole(String.valueOf(UserRole.ADMIN))

                .requestMatchers(HttpMethod.GET, EMPLOYEE_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.POST, EMPLOYEE_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.PUT, EMPLOYEE_URL).hasRole(String.valueOf(UserRole.ADMIN))

                .requestMatchers(HttpMethod.GET, JOB_ROLE_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.POST, JOB_ROLE_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.PUT, JOB_ROLE_URL).hasRole(String.valueOf(UserRole.ADMIN))

                .requestMatchers(HttpMethod.GET, "/leave/get/**").hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.GET, "/leave/get-own/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, "/leave/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, "/leave/cancel/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, "/leave/approve/**").hasRole(String.valueOf(UserRole.ADMIN))

                .requestMatchers(HttpMethod.GET, "/payroll/get/**").hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.GET, "/payroll/get-own/**").hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, PAYROLL_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.PUT, PAYROLL_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.DELETE, PAYROLL_URL).hasRole(String.valueOf(UserRole.ADMIN))

                .anyRequest().authenticated()
        );

        http.addFilterAfter(jwtTokenFilter, CorsFilter.class);
        http.exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) -> {
                        throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", authException.getMessage());
                    });
                    exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                        throw new ApplicationCustomException(HttpStatus.FORBIDDEN, "FORBIDDEN", accessDeniedException.getMessage());
                    });
                }
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

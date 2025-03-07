package com.xrontech.web.domain.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xrontech.web.config.CustomErrorResponse;
import com.xrontech.web.domain.security.domain.UserMapper;
import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.service.AuthService;
import com.xrontech.web.domain.security.util.JwtTokenUtil;
import com.xrontech.web.domain.user.UserService;
import com.xrontech.web.exception.ApplicationCustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final UserMapper mapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.split(" ")[1].trim();

        try {
            jwtTokenUtil.validateToken(token);
        } catch (ApplicationCustomException e) {
//            throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "Token Expired");
            CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.FORBIDDEN.value(), "ApplicationCustomException", "Unauthorized access");
            errorResponse.setHttpStatus(e.getStatus().value());
            errorResponse.setException(e.getClass().getSimpleName());
            errorResponse.setMessage(e.getMessage());
            response.setStatus(e.getStatus().value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
            return;
        }

        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        UserDetails userDetails = mapper.mapToUserDetails(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }
}

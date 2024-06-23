package com.xrontech.web.domain.security.service;

import com.xrontech.web.EmployeeManagementApplication;
import com.xrontech.web.domain.security.domain.UserData;
import com.xrontech.web.domain.security.dto.AuthResponseDTO;
import com.xrontech.web.domain.security.dto.LogInDTO;
import com.xrontech.web.domain.security.dto.ResetForgotPasswordDTO;
import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.repos.UserRepository;
import com.xrontech.web.domain.security.util.JwtTokenUtil;
import com.xrontech.web.domain.user.ResetPasswordDTO;
import com.xrontech.web.domain.user.UserService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import com.xrontech.web.mail.MailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailService mailService;

    public AuthResponseDTO login(LogInDTO logInDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(logInDTO.getUsername());
        if (optionalUser.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_USERNAME", "Invalid Username");
        } else {
            User user = optionalUser.get();
            if (!passwordEncoder.matches(logInDTO.getPassword(), user.getPassword())) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_PASSWORD", "Invalid Password");
            }

            checkAccountStatus(user);

            String accessToken = jwtTokenUtil.generateAccessToken(user);
            String refreshToken = jwtTokenUtil.generateRefreshToken(user);
            return new AuthResponseDTO(HttpStatus.OK, "LOGIN_SUCCESS", "Login Success", accessToken, refreshToken);
        }
    }

    public static String getCurrentUser() {
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            if (securityContext != null && securityContext.getAuthentication() != null) {
                Object principal = securityContext.getAuthentication().getPrincipal();
                if (principal instanceof UserData userData) {
                    return userData.getUsername();
                } else {
                    throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "INVALID_PRINCIPAL", "Invalid Principal");
                }
            } else {
                throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "SECURITY_CONTEXT_IS_NULL", "Security Context is Null");
            }
        } catch (Exception e) {
            throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "INVALID_USER", e.getMessage());
        }
    }

    public ApplicationResponseDTO resetPassword(ResetPasswordDTO resetPasswordDTO) {
        String username = AuthService.getCurrentUser();
        User user = userService.findByUsername(username);
        if (!passwordEncoder.matches(resetPasswordDTO.getOldPassword(), user.getPassword())) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_OLD_PASSWORD", "Invalid Old Password");
        } else if (!(resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword()))) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CONFIRM_PASSWORD_DOES_NOT_MATCH", "Confirm Password does not match");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userRepository.save(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "USER_NEW_PASSWORD_UPDATED_SUCCESSFULLY", "User New Password Updated Successfully!");
    }

    public AuthResponseDTO generateRefreshToken(String refreshToken) {
        if (jwtTokenUtil.validateToken(refreshToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not Found"));
            String accessToken = jwtTokenUtil.generateAccessToken(user);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(user);
            return new AuthResponseDTO(HttpStatus.OK, "NEW_ACCESS_TOKEN_&_NEW_REFRESH_TOKEN", "New Access & Refresh Token", accessToken, newRefreshToken);
        }
        throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "Invalid Refresh Token");
    }

    public ApplicationResponseDTO forgotPassword(String email, HttpServletRequest request) {
        User user = userRepository.findByUsername(email).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "EMAIL_NOT_FOUND", "Email not found")
        );

        checkAccountStatus(user);

        String baseUrl = String.format("%s://%s:%d",request.getScheme(),request.getServerName(),request.getServerPort());
        String resetPasswordLink = baseUrl + "/auth/reset-password/"+user.getId();

        mailService.sendForgotPasswordMail("Reset Password",email,user.getName(),resetPasswordLink);

        return new ApplicationResponseDTO(HttpStatus.OK, "FORGOT_PASSWORD_SENT_SUCCESSFULLY", "Forgot Password Link Sent To Your Email ");
    }

    public void checkAccountStatus(User user){
        if (user.getDelete()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ACCOUNT_DELETED", "Account Deleted");
        }

        if (!user.getStatus()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ACCOUNT_DISABLED", "Account Disabled");
        }
    }

    public ApplicationResponseDTO resetForgotPassword(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found")
        );
        checkAccountStatus(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "VALID_RESET_PASSWORD_LINK", "Valid Reset Password Link!");
    }
    public ApplicationResponseDTO resetForgotPassword(Long id, ResetForgotPasswordDTO resetForgotPasswordDTO) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found")
        );
        checkAccountStatus(user);

        if (!(resetForgotPasswordDTO.getNewPassword().equals(resetForgotPasswordDTO.getConfirmPassword()))) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CONFIRM_PASSWORD_DOES_NOT_MATCH", "Confirm Password does not match");
        }
        user.setPassword(passwordEncoder.encode(resetForgotPasswordDTO.getNewPassword()));
        userRepository.save(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "USER_NEW_PASSWORD_UPDATED_SUCCESSFULLY", "User New Password Updated Successfully!");
    }
}

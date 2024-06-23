package com.xrontech.web.domain.user;

import com.xrontech.web.domain.security.service.AuthService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;
    private final AuthService authService;

    @PutMapping("/update")
    public ResponseEntity<ApplicationResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userService.updateUser(userUpdateDTO));
    }
    @PutMapping("/update-profile-pic")
    public ResponseEntity<ApplicationResponseDTO> updateProfilePic(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfilePic(file));
    }
    @PutMapping("/reset-password")
    public ResponseEntity<ApplicationResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return ResponseEntity.ok(authService.resetPassword(resetPasswordDTO));
    }
}

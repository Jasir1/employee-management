package com.xrontech.web.domain.user;

import com.xrontech.web.domain.employee.EmployeeDTO;
import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.service.AuthService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "ems")
public class UserResource {
    private final UserService userService;

    @PutMapping("/update")
    public ResponseEntity<ApplicationResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userService.updateUser(userUpdateDTO));
    }
    @PutMapping("/update-profile-pic")
    public ResponseEntity<ApplicationResponseDTO> updateProfilePic(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfilePic(file));
    }
    @GetMapping("/get")
    public ResponseEntity<User> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }
}

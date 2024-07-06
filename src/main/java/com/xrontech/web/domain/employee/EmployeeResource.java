package com.xrontech.web.domain.employee;

import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.service.AuthService;
import com.xrontech.web.domain.user.ResetPasswordDTO;
import com.xrontech.web.domain.user.UserUpdateDTO;
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
@RequestMapping("/employee")
@SecurityRequirement(name = "ems")
public class EmployeeResource {
    private final EmployeeService employeeService;

    @PostMapping("/create")
    public ResponseEntity<ApplicationResponseDTO> createUser(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.createUser(employeeDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(employeeService.getAllUsers());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(employeeService.getUser(id));
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<ApplicationResponseDTO> changeUserStatus(@PathVariable("id") Long id) {
        return ResponseEntity.ok(employeeService.changeUserStatus(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApplicationResponseDTO> deleteUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(employeeService.deleteUser(id));
    }
}

package com.xrontech.web.domain.job;

import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/job-role")
@SecurityRequirement(name = "ems")
public class JobRoleResource {
    private final JobRoleService jobRoleService;

    @PostMapping("/create")
    public ResponseEntity<ApplicationResponseDTO> createJobRole(@Valid @RequestBody JobRoleDTO jobRoleDTO){
        return ResponseEntity.ok(jobRoleService.createJobRole(jobRoleDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<JobRole>> getAllJobRoles(){
        return ResponseEntity.ok(jobRoleService.getAllJobRoles());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<JobRole> getJobRoleById(@PathVariable("id") Long id){
        return ResponseEntity.ok(jobRoleService.getJobRoleById(id));
    }

    @GetMapping("/get/title/{title}")
    public ResponseEntity<List<JobRole>> getJobRoleByTitle(@PathVariable("title") String title){
        return ResponseEntity.ok(jobRoleService.getJobRoleByTitle(title));
    }

    @GetMapping("/get/department/{id}")
    public ResponseEntity<List<JobRole>> getJobRoleByDepartment(@PathVariable("id") Long id){
        return ResponseEntity.ok(jobRoleService.getJobRoleByDepartment(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateJobRole(@PathVariable("id") Long id, @Valid @RequestBody JobRoleDTO jobRoleDTO){
        return ResponseEntity.ok(jobRoleService.updateJobRole(id,jobRoleDTO));
    }
}

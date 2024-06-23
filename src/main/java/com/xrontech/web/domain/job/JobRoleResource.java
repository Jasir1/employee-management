package com.xrontech.web.domain.job;

import com.xrontech.web.domain.department.Department;
import com.xrontech.web.domain.department.DepartmentDTO;
import com.xrontech.web.domain.department.DepartmentService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/job-role")
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

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<JobRole> getJobRoleById(@PathVariable("id") Long id){
        return ResponseEntity.ok(jobRoleService.getJobRoleById(id));
    }

    @GetMapping("/get-by-title/{title}")
    public ResponseEntity<List<JobRole>> getJobRoleByTitle(@PathVariable("title") String title){
        return ResponseEntity.ok(jobRoleService.getJobRoleByTitle(title));
    }

    @GetMapping("/get-by-department/{id}")
    public ResponseEntity<List<JobRole>> getJobRoleByDepartment(@PathVariable("id") Long id){
        return ResponseEntity.ok(jobRoleService.getJobRoleByDepartment(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateJobRole(@PathVariable("id") Long id, @Valid @RequestBody JobRoleDTO jobRoleDTO){
        return ResponseEntity.ok(jobRoleService.updateJobRole(id,jobRoleDTO));
    }
}

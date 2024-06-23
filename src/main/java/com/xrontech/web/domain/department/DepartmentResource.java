package com.xrontech.web.domain.department;

import com.xrontech.web.dto.ApplicationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/department")
public class DepartmentResource {
    private final DepartmentService departmentService;

    @PostMapping("/create")
    public ResponseEntity<ApplicationResponseDTO> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO){
        return ResponseEntity.ok(departmentService.createDepartment(departmentDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Department>> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable("id") Long id){
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping("/get-by-name/{name}")
    public ResponseEntity<Department> getDepartmentByName(@PathVariable("name") String name){
        return ResponseEntity.ok(departmentService.getDepartmentByName(name));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateDepartment(@PathVariable("id") Long id, @Valid @RequestBody DepartmentDTO departmentDTO){
        return ResponseEntity.ok(departmentService.updateDepartment(id,departmentDTO));
    }
}

package com.xrontech.web.domain.department;

import com.xrontech.web.EmployeeManagementApplication;
import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    public ApplicationResponseDTO createDepartment(DepartmentDTO departmentDTO) {
        if (departmentRepository.findByName(departmentDTO.getName()).isPresent()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "DEPARTMENT_ALREADY_EXIST", "Department already exist");
        }

        departmentRepository.save(Department.builder().name(departmentDTO.getName()).build());
        return new ApplicationResponseDTO(HttpStatus.CREATED, "NEW_DEPARTMENT_CREATED_SUCCESSFULLY", "New department created successfully");
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAllByNameNot("Admin");
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(()-> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_DEPARTMENT_ID", "Invalid department id"));
    }

    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name).orElseThrow(()-> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_DEPARTMENT_NAME", "Invalid department name"));
    }

    public ApplicationResponseDTO updateDepartment(Long id,DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(id).orElseThrow(() ->
            new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_DEPARTMENT_ID", "Invalid department id")
        );

        departmentRepository.findByName(departmentDTO.getName()).ifPresent(existingDepartment -> {
            if (!department.getName().equals(departmentDTO.getName())) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "DEPARTMENT_ALREADY_EXISTS", "Department already exists");
            }
        });

        department.setName(departmentDTO.getName());

        departmentRepository.save(department);
        return new ApplicationResponseDTO(HttpStatus.CREATED, "DEPARTMENT_UPDATED_SUCCESSFULLY", "Department updated successfully");
    }
}

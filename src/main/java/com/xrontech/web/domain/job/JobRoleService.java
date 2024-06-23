package com.xrontech.web.domain.job;

import com.xrontech.web.domain.department.DepartmentRepository;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobRoleService {
    private final JobRoleRepository jobRoleRepository;
    private final DepartmentRepository departmentRepository;

    public ApplicationResponseDTO createJobRole(JobRoleDTO jobRoleDTO) {
        if (jobRoleRepository.findByTitleAndDepartmentId(jobRoleDTO.getTitle(),jobRoleDTO.getDepartmentId()).isPresent()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "JOB_ROLE_ALREADY_EXIST", "Job role already exist");
        }

        if (departmentRepository.findById(jobRoleDTO.getDepartmentId()).isEmpty()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_DEPARTMENT_ID", "Invalid department id");
        }

        jobRoleRepository.save(JobRole.builder().title(jobRoleDTO.getTitle()).salary(jobRoleDTO.getSalary()).departmentId(jobRoleDTO.getDepartmentId()).build());
        return new ApplicationResponseDTO(HttpStatus.CREATED, "NEW_JOB_ROLE_CREATED_SUCCESSFULLY", "New job role created successfully");
    }

    public List<JobRole> getAllJobRoles() {
        return jobRoleRepository.findAllByTitleNot("Admin");
    }

    public JobRole getJobRoleById(Long id) {
        return jobRoleRepository.findById(id).orElseThrow(()-> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_JOB_ROLE_ID", "Invalid job role id"));
    }

    public List<JobRole> getJobRoleByTitle(String title) {

        List<JobRole> jobRoles = jobRoleRepository.findAllByTitle(title);
        if (jobRoles.isEmpty()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_JOB_ROLE_TITLE", "Invalid job role title");
        }
        return jobRoles;
    }

    public List<JobRole> getJobRoleByDepartment(Long departmentId) {

        if (departmentRepository.findById(departmentId).isEmpty()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_DEPARTMENT_ID", "Invalid department id");
        }
        return jobRoleRepository.findByDepartmentId(departmentId);
    }

    public ApplicationResponseDTO updateJobRole(Long id,JobRoleDTO jobRoleDTO) {

        JobRole jobRole = jobRoleRepository.findById(id).orElseThrow(() ->
            new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_JOB_ROLE_ID", "Invalid job role id")
        );

        jobRoleRepository.findByTitleAndDepartmentId(jobRoleDTO.getTitle(),id).ifPresent(existingDepartment -> {
            if (!jobRole.getTitle().equals(jobRoleDTO.getTitle())) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "JOB_ROLE_ALREADY_EXISTS", "Job role already exists");
            }
        });

        jobRole.setTitle(jobRoleDTO.getTitle());
        jobRole.setSalary(jobRoleDTO.getSalary());
        jobRole.setDepartmentId(jobRoleDTO.getDepartmentId());

        jobRoleRepository.save(jobRole);
        return new ApplicationResponseDTO(HttpStatus.CREATED, "JOB_ROLE_UPDATED_SUCCESSFULLY", "Job role updated successfully");
    }
}

package com.xrontech.web;

import com.xrontech.web.domain.department.Department;
import com.xrontech.web.domain.department.DepartmentRepository;
import com.xrontech.web.domain.job.JobRole;
import com.xrontech.web.domain.job.JobRoleRepository;
import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.entity.UserRole;
import com.xrontech.web.domain.security.repos.UserRepository;
import com.xrontech.web.domain.security.service.AuthService;
import com.xrontech.web.exception.ApplicationCustomException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Employees API", version = "2.0", description = "Employees Information"))
@SecurityScheme(name = "ems", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class EmployeeManagementApplication {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final JobRoleRepository jobRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
    }

    @PostConstruct
    public void init(){

        Department department = departmentRepository.findByName("Admin").orElseGet(() -> departmentRepository.save(
                Department.builder()
                        .name("Admin").build()
        ));

        JobRole jobRole = jobRoleRepository.findByTitle("Admin").orElseGet(() -> jobRoleRepository.save(
                JobRole.builder()
                        .title("Admin")
                        .salary(0.0)
                        .departmentId(department.getId())
                        .build()
        ));

        if (userRepository.findByUsername("mhdjasir4565@gmail.com").isEmpty()){
            userRepository.save(
                    User.builder()
                            .name("Jasir")
                            .username("mhdjasir4565@gmail.com")
                            .mobile("0762684595")
                            .password(passwordEncoder.encode("1234"))
                            .active(true)
                            .delete(false)
                            .userRole(UserRole.ADMIN)
                            .jobId(jobRole.getId())
                            .build()
            );
        }

        if (userRepository.findByUsername("mhdjasir3454@gmail.com").isEmpty()){
            userRepository.save(
                    User.builder()
                            .name("mohamed")
                            .username("mhdjasir3454@gmail.com")
                            .mobile("0762684596")
                            .password(passwordEncoder.encode("1234"))
                            .active(true)
                            .delete(false)
                            .userRole(UserRole.USER)
                            .jobId(jobRole.getId())
                            .build()
            );
        }
    }


}

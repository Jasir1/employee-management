package com.xrontech.web.domain.job;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobRoleDTO {
    @NotBlank
    private String title;

    @NotNull
    @Min(0)
    private Double salary;

    @NotNull
    private Long departmentId;
}

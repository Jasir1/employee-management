package com.xrontech.web.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdateDTO {
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 10, max = 10)
    private String mobile;
    private LocalDate dateOfBirth;
}

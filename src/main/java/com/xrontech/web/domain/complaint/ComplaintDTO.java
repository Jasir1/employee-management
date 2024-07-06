package com.xrontech.web.domain.complaint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ComplaintDTO {
    @NotBlank
    private String description;

    @NotNull
    private ComplaintType type;

}

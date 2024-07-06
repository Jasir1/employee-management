package com.xrontech.web.domain.document;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentDTO {

    @NotBlank
    private String title;

    private DocumentType type;

    @NotNull
    private Long employeeId;
}

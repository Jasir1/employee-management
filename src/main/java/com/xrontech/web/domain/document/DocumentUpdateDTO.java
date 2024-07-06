package com.xrontech.web.domain.document;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentUpdateDTO {

    @NotBlank
    private String title;

    private DocumentType type;

}

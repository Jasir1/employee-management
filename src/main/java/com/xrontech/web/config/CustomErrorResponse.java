package com.xrontech.web.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomErrorResponse {
    private Integer httpStatus;
    private String exception;
    private String message;
}

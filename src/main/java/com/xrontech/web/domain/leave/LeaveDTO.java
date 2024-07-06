package com.xrontech.web.domain.leave;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LeaveDTO {
    @NotNull
    @Future
    private LocalDate fromDate;

    @NotNull
    @Future
    private LocalDate toDate;

    @NotNull
    private LeaveType type;

}

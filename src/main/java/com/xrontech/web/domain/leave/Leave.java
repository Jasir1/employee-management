package com.xrontech.web.domain.leave;

import com.xrontech.web.domain.model.BaseEntity;
import com.xrontech.web.domain.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Leave extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LeaveType type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LeaveStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id",referencedColumnName = "id",insertable = false,updatable = false)
    private User employee;

    @Column(name = "employee_id")
    private Long employeeId;

}

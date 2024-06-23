package com.xrontech.web.domain.security.entity;

import com.xrontech.web.domain.job.JobRole;
import com.xrontech.web.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "\"User\"")
public class User extends BaseEntity {
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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "mobile", nullable = false)
    private String mobile;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "delete", nullable = false)
    private Boolean delete = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "job_role_id")
    private Long jobId;

    @ManyToOne
    @JoinColumn(name = "job_role_id",referencedColumnName = "id",insertable = false,updatable = false)
    private JobRole jobRole;
}

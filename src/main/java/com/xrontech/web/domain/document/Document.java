package com.xrontech.web.domain.document;

import com.xrontech.web.domain.model.BaseEntity;
import com.xrontech.web.domain.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Document extends BaseEntity {
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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @Column(name = "url",columnDefinition = "TEXT")
    private String url;

    @ManyToOne
    @JoinColumn(name = "employee_id",referencedColumnName = "id",insertable = false,updatable = false)
    private User employee;

    @Column(name = "employee_id")
    private Long employeeId;

}

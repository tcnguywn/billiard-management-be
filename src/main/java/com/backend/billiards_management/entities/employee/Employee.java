package com.backend.billiards_management.entities.employee;

import com.backend.billiards_management.entities.BaseEntity;
import com.backend.billiards_management.entities.employee.enums.Role;
import com.backend.billiards_management.entities.image.UploadImage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends BaseEntity {

    @Column(name = "keycloak_id", unique = true, nullable = false)
    private String keycloakId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private UploadImage image;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
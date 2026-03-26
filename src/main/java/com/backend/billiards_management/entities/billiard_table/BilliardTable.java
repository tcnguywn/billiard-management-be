package com.backend.billiards_management.entities.billiard_table;

import com.backend.billiards_management.dtos.constant.TableType;
import com.backend.billiards_management.entities.BaseEntity;
import com.backend.billiards_management.entities.billiard_table.enums.TableStatus;
import com.backend.billiards_management.entities.image.UploadImage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "billiard_tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BilliardTable extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TableStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "table_type")
    private TableType tableType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private UploadImage uploadImage;
}
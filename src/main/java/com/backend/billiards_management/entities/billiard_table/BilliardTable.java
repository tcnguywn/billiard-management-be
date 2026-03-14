package com.backend.billiards_management.entities.billiard_table;

import com.backend.billiards_management.entities.BaseEntity;
import com.backend.billiards_management.entities.image.UploadImage;
import com.backend.billiards_management.entities.table_type.TableType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "billiard_tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BilliardTable extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_type_id")
    private TableType tableType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private UploadImage uploadImage;
}
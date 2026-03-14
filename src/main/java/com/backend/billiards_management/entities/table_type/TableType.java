package com.backend.billiards_management.entities.table_type;

import com.backend.billiards_management.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "table_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableType extends BaseEntity {
    @Column(name = "type_name")
    private String typeName;
}
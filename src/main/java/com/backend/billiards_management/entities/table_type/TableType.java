package com.backend.billiards_management.entities.table_type;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "table_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private int id;

    @Column(name = "type_name")
    private String typeName;
}
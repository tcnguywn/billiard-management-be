package com.backend.billiards_management.entities.price_list;

import com.backend.billiards_management.entities.BaseEntity;
import com.backend.billiards_management.entities.table_type.TableType;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "price_lists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceList extends BaseEntity {

    @Temporal(TemporalType.TIME)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(TemporalType.TIME)
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_type_id")
    private TableType tableType;
}
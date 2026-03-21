package com.backend.billiards_management.entities.voucher;

import com.backend.billiards_management.entities.BaseEntity;
import com.backend.billiards_management.entities.voucher.enums.VoucherSource;
import com.backend.billiards_management.entities.voucher.enums.VoucherStatus;
import com.backend.billiards_management.entities.voucher.enums.VoucherType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "vouchers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher extends BaseEntity {

    @Column(name = "voucher_code", unique = true)
    private String voucherCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VoucherType type;

    @Column(name = "value")
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", length = 20)
    private VoucherSource source;

    @Column(name = "status")
    private VoucherStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "minimum_amount", precision = 10, scale = 2)
    private BigDecimal minimumAmount;

    @Column(name = "maximum_amount", precision = 10, scale = 2)
    private BigDecimal maximumValue;
}

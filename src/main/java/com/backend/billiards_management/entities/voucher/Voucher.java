package com.backend.billiards_management.entities.voucher;

import com.backend.billiards_management.entities.voucher.enums.VoucherStatus;
import com.backend.billiards_management.entities.voucher.enums.VoucherType;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "vouchers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "voucher_code", unique = true)
    private String voucherCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VoucherType type;

    @Column(name = "value")
    private Double value;

    @Column(name = "source")
    private String source;

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

    @Column(name = "minimum_amount")
    private Double minimumAmount;
}

package com.backend.billiards_management.entities.bank;

import com.backend.billiards_management.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bank extends BaseEntity {
    @Column(name = "bank_bin", unique = true)
    private String bankBin;

    @Column(name = "bank_account_no")
    private String bankAccountNo;

    @Column(name = "bank_account_name")
    private String bankAccountName;

    @Column(name = "bank_status")
    private boolean bankStatus;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_short_name")
    private String bankShortName;

    @Column(name = "bank_logo")
    private String bankLogo;
}

package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.voucher.Voucher;
import com.backend.billiards_management.entities.voucher.enums.VoucherStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findAllByStatus(VoucherStatus status);
}

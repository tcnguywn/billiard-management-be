package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.invoice.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Page<Invoice> findByCreatedAtBetweenAndDeletedFalse(Date startDate, Date endDate, Pageable pageable);

    Optional<Invoice> findByIdAndDeletedFalse(Integer invoiceId);
}

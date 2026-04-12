package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.purchase_invoice.PurchaseInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice, Integer> {
    List<PurchaseInvoice> findByImportDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}

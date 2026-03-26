package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.purchase_invoice.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice, Integer> {
}

package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.purchase_detail.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetail, Integer> {
}

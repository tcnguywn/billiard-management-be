package com.backend.billiards_management.services.purchase_invoice;

import com.backend.billiards_management.dtos.request.purchase_invoice.PurchaseInvoiceReq;
import com.backend.billiards_management.dtos.response.purchase.PurchaseInvoiceRes;
import com.backend.billiards_management.dtos.response.purchase.PurchaseInvoiceResDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface PurchaseInvoiceService {
    PurchaseInvoiceResDetail createPurchaseInvoice(PurchaseInvoiceReq req);
    PurchaseInvoiceResDetail getPurchaseInvoiceById(int id);
    void deletePurchaseInvoice(int id);
    List<PurchaseInvoiceRes> searchByDateRange(LocalDate startDate, LocalDate endDate);
    Page<PurchaseInvoiceRes> getAllPurchaseInvoices(Pageable pageable);
}

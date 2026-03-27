package com.backend.billiards_management.services.purchase_invoice;

import com.backend.billiards_management.dtos.request.purchase_invoice.PurchaseInvoiceReq;
import com.backend.billiards_management.dtos.response.purchase.PurchaseInvoiceRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PurchaseInvoiceService {
    PurchaseInvoiceRes createPurchaseInvoice(PurchaseInvoiceReq req);
    PurchaseInvoiceRes getPurchaseInvoiceById(int id);
    void deletePurchaseInvoice(int id);
    Page<PurchaseInvoiceRes> getAllPurchaseInvoices(Pageable pageable);
}

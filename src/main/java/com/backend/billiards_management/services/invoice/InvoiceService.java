package com.backend.billiards_management.services.invoice;

import com.backend.billiards_management.dtos.request.invoice.InvoiceReq;
import com.backend.billiards_management.dtos.response.invoice.InvoiceRes;
import org.springframework.data.domain.Page;

public interface InvoiceService {
    Page<InvoiceRes> getInvoicesByRange(InvoiceReq invoiceReq);
}

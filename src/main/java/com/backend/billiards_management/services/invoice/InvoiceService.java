package com.backend.billiards_management.services.invoice;

import com.backend.billiards_management.dtos.request.invoice.CreateInvoiceReq;
import com.backend.billiards_management.dtos.request.invoice.UpdateInvoiceReq;
import com.backend.billiards_management.dtos.response.invoice.InvoiceRes;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {
    InvoiceRes createInvoice(CreateInvoiceReq req);
    InvoiceRes updateInvoice(UpdateInvoiceReq req);
    InvoiceRes getInvoiceById(int id);
    List<InvoiceRes> getAllInvoices();
    void deleteInvoice(int id);
    List<InvoiceRes> getInvoicesByStatus(String status);
    List<InvoiceRes> getInvoicesByEmployeeId(int employeeId);
    void confirmInvoice(int id);

    List<InvoiceRes> getInvoicesByRange(LocalDate startDate, LocalDate endDate);
}

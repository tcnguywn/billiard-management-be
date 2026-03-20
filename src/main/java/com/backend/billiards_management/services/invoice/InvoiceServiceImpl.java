package com.backend.billiards_management.services.invoice;

import com.backend.billiards_management.dtos.request.invoice.InvoiceReq;
import com.backend.billiards_management.dtos.response.invoice.InvoiceRes;
import com.backend.billiards_management.entities.invoice.Invoice;
import com.backend.billiards_management.repositories.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<InvoiceRes> getInvoicesByRange(InvoiceReq invoiceReq) {
        Pageable pageable = PageRequest.of(invoiceReq.getPage(), invoiceReq.getSize());

        Page<Invoice> invoiceList = invoiceRepository.findByCreatedAtBetweenAndDeletedFalse(
                invoiceReq.getStartDate(),
                invoiceReq.getEndDate(),
                pageable
        );

        return invoiceList.map(invoice -> modelMapper.map(invoice, InvoiceRes.class));
    }
}

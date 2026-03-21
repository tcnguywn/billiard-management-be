package com.backend.billiards_management.services.invoice;

import com.backend.billiards_management.dtos.request.invoice.InvoiceDetailRes;
import com.backend.billiards_management.dtos.request.invoice.InvoiceReq;
import com.backend.billiards_management.dtos.response.invoice.InvoiceRes;
import com.backend.billiards_management.entities.invoice.Invoice;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<InvoiceRes> getInvoicesByRange(InvoiceReq invoiceReq) {

        if (invoiceReq.getPage() < 0) {
            throw new IllegalArgumentException("Page must be greater than or equal to 0");
        }

        if (invoiceReq.getSize() <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        if (invoiceReq.getEndDate().before(invoiceReq.getStartDate())) {
            throw new IllegalArgumentException("End date must be before start date");
        }

        Pageable pageable = PageRequest.of(invoiceReq.getPage(), invoiceReq.getSize());

        Page<Invoice> invoiceList = invoiceRepository.findByCreatedAtBetweenAndDeletedFalse(
                invoiceReq.getStartDate(),
                invoiceReq.getEndDate(),
                pageable
        );

        return invoiceList.map(invoice -> modelMapper.map(invoice, InvoiceRes.class));
    }

    @Override
    public InvoiceDetailRes getInvoiceById(Integer invoiceId) {

        Invoice invoice = invoiceRepository.findByIdAndDeletedFalse(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Invoice not found"));

        InvoiceDetailRes invoiceDetailRes = modelMapper.map(invoice, InvoiceDetailRes.class);

        if (invoice.getBilliardTable() == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Billiard table not found");
        }

//        TODO: Set thêm các field còn thiếu
        invoiceDetailRes.setTableName(invoice.getBilliardTable().getName());
        invoiceDetailRes.setPlaytime(
                String.valueOf(
                        (invoice.getEndTime().getTime() - invoice.getStartTime().getTime()) / 3600000.0
                )
        );
        invoiceDetailRes.setPlayAmount(BigDecimal.valueOf(0));
        invoiceDetailRes.setTempAmount(
                invoiceDetailRes.getPlayAmount()
                        .add(invoiceDetailRes.getServiceAmount())
                        .add(invoiceDetailRes.getProductAmount())
        );


        return invoiceDetailRes;
    }
}

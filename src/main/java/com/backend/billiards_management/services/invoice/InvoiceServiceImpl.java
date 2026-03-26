package com.backend.billiards_management.services.invoice;

import com.backend.billiards_management.repositories.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;
}

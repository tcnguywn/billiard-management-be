package com.backend.billiards_management.services.purchase_invoice;

import com.backend.billiards_management.dtos.request.purchase_invoice.PurchaseDetailReq;
import com.backend.billiards_management.dtos.request.purchase_invoice.PurchaseInvoiceReq;
import com.backend.billiards_management.dtos.response.purchase.PurchaseDetailRes;
import com.backend.billiards_management.dtos.response.purchase.PurchaseInvoiceRes;
import com.backend.billiards_management.dtos.response.purchase.PurchaseInvoiceResDetail;
import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.entities.product.Product;
import com.backend.billiards_management.entities.purchase_detail.PurchaseDetail;
import com.backend.billiards_management.entities.purchase_invoice.PurchaseInvoice;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.EmployeeRepository;
import com.backend.billiards_management.repositories.ProductRepository;
import com.backend.billiards_management.repositories.PurchaseInvoiceRepository;
import com.backend.billiards_management.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {

    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final EmployeeService  employeeService;
    @Override
    public PurchaseInvoiceResDetail createPurchaseInvoice(PurchaseInvoiceReq req) {
        Employee employee = employeeRepository.findById(employeeService.getMyProfile().getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Cannot find employee"));

        PurchaseInvoice invoice = PurchaseInvoice.builder()
                .employee(employee)
                .importDate(LocalDateTime.now())
                .purchaseDetails(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;
        for(PurchaseDetailReq detailReq : req.getDetails()) {
            Product product = productRepository.findById((long) detailReq.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Cannot find product with id: " + detailReq.getProductId()));

            int currentStock = product.getStock() != null ? product.getStock() : 0;
            product.setStock(currentStock + detailReq.getQuantity());

            product.setImportPrice(detailReq.getImportPrice());

            productRepository.save(product);

            PurchaseDetail detail = PurchaseDetail.builder()
                    .purchaseInvoice(invoice)
                    .product(product)
                    .quantity(detailReq.getQuantity())
                    .importPrice(detailReq.getImportPrice())
                    .build();

            invoice.getPurchaseDetails().add(detail);

            BigDecimal subTotal = detailReq.getImportPrice().multiply(BigDecimal.valueOf(detailReq.getQuantity()));
            totalAmount = totalAmount.add(subTotal);
        }
//        invoice.setPurchaseDetails(purchaseDetails);
        invoice.setTotalAmount(totalAmount);

        PurchaseInvoice savedInvoice = purchaseInvoiceRepository.save(invoice);
        return mapToResponse(savedInvoice);
    }

    @Override
    public PurchaseInvoiceResDetail getPurchaseInvoiceById(int id) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Cannot find purchase invoice with id: " + id));

        return mapToResponse(invoice);
    }

    @Override
    public void deletePurchaseInvoice(int id) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Cannot find purchase invoice with id: " + id));

        purchaseInvoiceRepository.delete(invoice);
    }

    @Override
    public Page<PurchaseInvoiceRes> getAllPurchaseInvoices(Pageable pageable) {
        return purchaseInvoiceRepository.findAll(pageable)
                .map(invoice -> PurchaseInvoiceRes.builder()
                        .purchaseId(invoice.getId())
                        .totalPrice(invoice.getTotalAmount())
                        .purchaseDate(invoice.getImportDate())
                        .build());
    }


    private PurchaseInvoiceResDetail mapToResponse(PurchaseInvoice invoice) {
        List<PurchaseDetailRes> detailResponses = invoice.getPurchaseDetails().stream()
                .map(detail -> PurchaseDetailRes.builder()
                        .id(detail.getId())
                        .productId(detail.getProduct().getId())
                        .productName(detail.getProduct().getName())
                        .quantity(detail.getQuantity())
                        .importPrice(detail.getImportPrice())
                        .subTotal(detail.getImportPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        String employeeName = invoice.getEmployee().getLastName() + " " + invoice.getEmployee().getFirstName();

        return PurchaseInvoiceResDetail.builder()
                .id(invoice.getId())
                .totalAmount(invoice.getTotalAmount())
                .importDate(invoice.getImportDate())
                .employeeId(invoice.getEmployee().getId())
                .employeeName(employeeName.trim())
                .details(detailResponses)
                .build();
    }
}

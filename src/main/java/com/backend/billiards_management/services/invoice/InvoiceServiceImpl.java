package com.backend.billiards_management.services.invoice;

import com.backend.billiards_management.dtos.request.invoice.CreateInvoiceReq;
import com.backend.billiards_management.dtos.request.invoice.UpdateInvoiceReq;
import com.backend.billiards_management.dtos.response.invoice.InvoiceRes;
import com.backend.billiards_management.dtos.response.order_detail.OrderDetailRes;
import com.backend.billiards_management.entities.billiard_table.BilliardTable;
import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.entities.invoice.Invoice;
import com.backend.billiards_management.entities.invoice.enums.PaymentMethod;
import com.backend.billiards_management.entities.invoice.enums.PaymentStatus;
import com.backend.billiards_management.entities.order_detail.OrderDetail;
import com.backend.billiards_management.entities.voucher.Voucher;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.EmployeeRepository;
import com.backend.billiards_management.repositories.InvoiceRepository;
import com.backend.billiards_management.repositories.TableRepository;
import com.backend.billiards_management.repositories.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final EmployeeRepository employeeRepository;
    private final TableRepository tableRepository;
    private final VoucherRepository voucherRepository;

    @Override
    @Transactional
    public InvoiceRes createInvoice(CreateInvoiceReq req) {
        // Tìm employee
        Employee employee = employeeRepository.findById(req.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find employee with id: " + req.getEmployeeId()));

        // Tìm billiard table
        BilliardTable billiardTable = tableRepository.findById(req.getBilliardTableId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find billiard table with id: " + req.getBilliardTableId()));

        // Tìm voucher nếu có
        Voucher voucher = null;
        if (req.getVoucherId() != 0) {
            voucher = voucherRepository.findById((long) req.getVoucherId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                            "Cannot find voucher with id: " + req.getVoucherId()));
        }

        // Parse payment method
        PaymentMethod paymentMethod = parsePaymentMethod(req.getPaymentMethod());

        Invoice invoice = Invoice.builder()
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .status(PaymentStatus.UNPAID)
                .paymentMethod(paymentMethod)
                .serviceAmount(req.getServiceAmount())
                .productAmount(req.getProductAmount())
                .taxAmount(req.getTaxAmount())
                .totalAmount(req.getTotalAmount())
                .voucher(voucher)
                .employee(employee)
                .billiardTable(billiardTable)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return mapToRes(savedInvoice);
    }

    @Override
    @Transactional
    public InvoiceRes updateInvoice(UpdateInvoiceReq req) {
        Invoice invoice = invoiceRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find invoice with id: " + req.getId()));

        if (invoice.isDeleted()) {
            throw new AppException(ErrorCode.NOT_FOUND, "Invoice with id " + req.getId() + " has been deleted");
        }

        // Cập nhật employee nếu có
        if (req.getEmployeeId() != 0) {
            Employee employee = employeeRepository.findById(req.getEmployeeId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                            "Cannot find employee with id: " + req.getEmployeeId()));
            invoice.setEmployee(employee);
        }

        // Cập nhật billiard table nếu có
        if (req.getBilliardTableId() != 0) {
            BilliardTable billiardTable = tableRepository.findById(req.getBilliardTableId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                            "Cannot find billiard table with id: " + req.getBilliardTableId()));
            invoice.setBilliardTable(billiardTable);
        }

        // Cập nhật voucher nếu có
        if (req.getVoucherId() != 0) {
            Voucher voucher = voucherRepository.findById((long) req.getVoucherId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                            "Cannot find voucher with id: " + req.getVoucherId()));
            invoice.setVoucher(voucher);
        }

        if (req.getStartTime() != null) {
            invoice.setStartTime(req.getStartTime());
        }
        if (req.getEndTime() != null) {
            invoice.setEndTime(req.getEndTime());
        }
        if (req.getStatus() != null) {
            invoice.setStatus(parsePaymentStatus(req.getStatus()));
        }
        if (req.getPaymentMethod() != null) {
            invoice.setPaymentMethod(parsePaymentMethod(req.getPaymentMethod()));
        }
        if (req.getServiceAmount() != null) {
            invoice.setServiceAmount(req.getServiceAmount());
        }
        if (req.getProductAmount() != null) {
            invoice.setProductAmount(req.getProductAmount());
        }
        if (req.getTaxAmount() != null) {
            invoice.setTaxAmount(req.getTaxAmount());
        }
        if (req.getTotalAmount() != null) {
            invoice.setTotalAmount(req.getTotalAmount());
        }

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return mapToRes(updatedInvoice);
    }

    @Override
    public InvoiceRes getInvoiceById(int id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find invoice with id: " + id));

        if (invoice.isDeleted()) {
            throw new AppException(ErrorCode.NOT_FOUND, "Invoice with id " + id + " has been deleted");
        }

        return mapToRes(invoice);
    }

    @Override
    public List<InvoiceRes> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findByDeletedFalse();
        List<InvoiceRes> invoiceResList = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceResList.add(mapToRes(invoice));
        }
        return invoiceResList;
    }

    @Override
    @Transactional
    public void deleteInvoice(int id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find invoice with id: " + id));

        if (invoice.isDeleted()) {
            throw new AppException(ErrorCode.NOT_FOUND, "Invoice with id " + id + " has already been deleted");
        }

        invoice.setDeleted(true);
        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceRes> getInvoicesByStatus(String status) {
        PaymentStatus paymentStatus = parsePaymentStatus(status);
        List<Invoice> invoices = invoiceRepository.findByStatusAndDeletedFalse(paymentStatus);
        List<InvoiceRes> invoiceResList = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceResList.add(mapToRes(invoice));
        }
        return invoiceResList;
    }

    @Override
    public List<InvoiceRes> getInvoicesByEmployeeId(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find employee with id: " + employeeId));

        List<Invoice> invoices = invoiceRepository.findByEmployeeAndDeletedFalse(employee);
        List<InvoiceRes> invoiceResList = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceResList.add(mapToRes(invoice));
        }
        return invoiceResList;
    }

    @Override
    public List<InvoiceRes> getInvoicesByRange(LocalDate startDate, LocalDate endDate) {

        ZoneId zone = ZoneId.systemDefault();

        Date start = Date.from(startDate.atStartOfDay(zone).toInstant());
        Date end = Date.from(endDate.atTime(LocalTime.MAX).atZone(zone).toInstant());

        List<Invoice> invoices = invoiceRepository.findByDeletedFalseAndCreatedAtBetween(start, end);
        List<InvoiceRes> invoiceResList = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceResList.add(mapToRes(invoice));
        }
        return invoiceResList;
    }

    // === Private helper methods ===

    private PaymentMethod parsePaymentMethod(String method) {
        if (method == null) return null;
        try {
            return PaymentMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.BAD_REQUEST,
                    "Invalid payment method: " + method + ". Accepted values: CASH, CREDIT_CARD");
        }
    }

    private PaymentStatus parsePaymentStatus(String status) {
        if (status == null) return null;
        try {
            return PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.BAD_REQUEST,
                    "Invalid payment status: " + status + ". Accepted values: PAID, UNPAID");
        }
    }

    /**
     * Map Invoice entity sang InvoiceRes DTO
     */
    private InvoiceRes mapToRes(Invoice invoice) {
        String employeeName = "";
        int employeeId = 0;
        if (invoice.getEmployee() != null) {
            employeeId = invoice.getEmployee().getId();
            employeeName = invoice.getEmployee().getFirstName() + " " + invoice.getEmployee().getLastName();
        }

        int voucherId = 0;
        String voucherCode = "";
        if (invoice.getVoucher() != null) {
            voucherId = invoice.getVoucher().getId();
            voucherCode = invoice.getVoucher().getVoucherCode();
        }

        int billiardTableId = 0;
        String billiardTableName = "";
        if (invoice.getBilliardTable() != null) {
            billiardTableId = invoice.getBilliardTable().getId();
            billiardTableName = invoice.getBilliardTable().getName();
        }

        List<OrderDetailRes> orderDetailResList = new ArrayList<>();
        for (OrderDetail i : invoice.getOrderDetailList()) {
            OrderDetailRes orderDetailRes = OrderDetailRes.builder()
                    .productName(i.getProduct().getName())
                    .quantity(i.getQuantity())
                    .totalPrice(i.getPrice())
                    .build();
            orderDetailResList.add(orderDetailRes);
        }

        return InvoiceRes.builder()
                .id(invoice.getId())
                .startTime(invoice.getStartTime())
                .endTime(invoice.getEndTime())
                .status(invoice.getStatus() != null ? invoice.getStatus().name() : null)
                .paymentMethod(invoice.getPaymentMethod() != null ? invoice.getPaymentMethod().name() : null)
                .serviceAmount(invoice.getServiceAmount())
                .productAmount(invoice.getProductAmount())
                .taxAmount(invoice.getTaxAmount())
                .totalAmount(invoice.getTotalAmount())
                .voucherId(voucherId)
                .voucherCode(voucherCode)
                .employeeId(employeeId)
                .employeeName(employeeName)
                .billiardTableId(billiardTableId)
                .billiardTableName(billiardTableName)
                .orderDetailResList(orderDetailResList)
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }
}

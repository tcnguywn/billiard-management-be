package com.backend.billiards_management.services.invoice;

import com.backend.billiards_management.dtos.constant.TableType;
import com.backend.billiards_management.dtos.request.invoice.CreateInvoiceReq;
import com.backend.billiards_management.dtos.request.invoice.UpdateInvoiceReq;
import com.backend.billiards_management.dtos.response.invoice.InvoiceRes;
import com.backend.billiards_management.dtos.response.order_detail.OrderDetailRes;
import com.backend.billiards_management.entities.billiard_table.BilliardTable;
import com.backend.billiards_management.entities.billiard_table.enums.TableStatus;
import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.entities.invoice.Invoice;
import com.backend.billiards_management.entities.invoice.enums.PaymentMethod;
import com.backend.billiards_management.entities.invoice.enums.PaymentStatus;
import com.backend.billiards_management.entities.order_detail.OrderDetail;
import com.backend.billiards_management.entities.price_list.PriceList;
import com.backend.billiards_management.entities.product_category.enums.ProductCategoryType;
import com.backend.billiards_management.entities.voucher.Voucher;
import com.backend.billiards_management.entities.voucher.enums.VoucherType;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.*;
import com.backend.billiards_management.services.bank.VietQRService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
    private final PricelistRepository pricelistRepository;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.10");

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

        Invoice invoice = Invoice.builder()
                .startTime(LocalDateTime.now())
                .endTime(null)
                .status(PaymentStatus.UNPAID)
                .paymentMethod(null)
                .serviceAmount(null)
                .productAmount(null)
                .taxAmount(null)
                .totalAmount(null)
                .voucher(null)
                .employee(employee)
                .billiardTable(billiardTable)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);
        billiardTable.setStatus(TableStatus.RESERVED);
        tableRepository.save(billiardTable);
        return mapToRes(savedInvoice);
    }

    @Override
    @Transactional
    public InvoiceRes updateInvoice(UpdateInvoiceReq req) {
        Invoice invoice = invoiceRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find invoice with id: " + req.getId()));

        if (invoice.getStatus() == PaymentStatus.PAID) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Invoice is paid");
        }

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

        Voucher voucher = null;
        if (req.getVoucherId() != 0) {
            voucher = voucherRepository.findById((long) req.getVoucherId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                            "Cannot find voucher with id: " + req.getVoucherId()));
            invoice.setVoucher(voucher);
        }

        if (req.getPaymentMethod() != null) {
            invoice.setPaymentMethod(parsePaymentMethod(req.getPaymentMethod()));
        }
        invoice.setEndTime(LocalDateTime.now());
        if (invoice.getStatus() == PaymentStatus.PAID)
            calculateInvoiceAmounts(invoice, voucher);
        BilliardTable table = invoice.getBilliardTable();
        table.setStatus(TableStatus.AVAILABLE);

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return mapToRes(updatedInvoice);
    }

    @Override
    @Transactional
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
    @Transactional
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
        for (OrderDetail detail : invoice.getOrderDetails()) {
            detail.setDeleted(true);
        }
        invoiceRepository.save(invoice);
    }
    @Transactional
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
        if (startDate == null || endDate == null) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Start date and end date cannot be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Start date cannot be after end date");
        }

        if (ChronoUnit.DAYS.between(startDate, endDate) > 30) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Date range cannot exceed 30 days");
        }

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

        List<OrderDetailRes> detailResList = new ArrayList<>();
        if (invoice.getOrderDetails() != null) {
            for (var d : invoice.getOrderDetails()) {
                if (d == null || d.isDeleted()) continue;
                var product = d.getProduct();
                var productCategory = product != null ? product.getProductCategory() : null;
                var type = productCategory != null ? productCategory.getType() : null;

                detailResList.add(OrderDetailRes.builder()
                        .id(d.getId())
                        .invoiceId(invoice.getId())
                        .productId(product != null ? product.getId() : null)
                        .productName(product != null ? product.getName() : null)
                        .price(d.getPrice())
                        .quantity(d.getQuantity())
                        .note(d.getNote())
                        .startTime(d.getStartTime())
                        .endTime(d.getEndTime())
                        .categoryType(type)
                        .deleted(d.isDeleted())
                        .build());
            }
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
                .orderDetails(detailResList)
                .voucherId(voucherId)
                .voucherCode(voucherCode)
                .employeeId(employeeId)
                .employeeName(employeeName)
                .billiardTableId(billiardTableId)
                .billiardTableName(billiardTableName)
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }

    @Override
    public void confirmInvoice(int id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find invoice with id: " + id));

        if (invoice.getStatus() != PaymentStatus.UNPAID) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Invoice with id " + id + " is already confirmed");
        }

        invoice.setStatus(PaymentStatus.PAID);
        invoiceRepository.save(invoice);
    }

    private void calculateInvoiceAmounts(Invoice invoice, Voucher voucher) {
        // Lấy danh sách order details của invoice - có thể null khi mới tạo
        List<OrderDetail> orderDetails = invoice.getOrderDetails();
        if (orderDetails == null) {
            orderDetails = new ArrayList<>();
        }

        BigDecimal productAmount = BigDecimal.ZERO;
        BigDecimal serviceAmount = BigDecimal.ZERO;

        for (OrderDetail detail : orderDetails) {
            // Bỏ qua nếu order detail bị xóa
            if (detail.isDeleted()) {
                continue;
            }

            // Bỏ qua nếu product hoặc product category là null
            if (detail.getProduct() == null || detail.getProduct().getProductCategory() == null) {
                continue;
            }

            if (detail.getProduct().getProductCategory().getType() == ProductCategoryType.RETAIL) {
                if (detail.getPrice() != null && detail.getQuantity() != null) {
                    BigDecimal lineTotal = detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
                    productAmount = productAmount.add(lineTotal);
                }
            } else {
                if (detail.getPrice() != null && detail.getStartTime() != null && detail.getEndTime() != null) {
                    // Validate endTime > startTime
                    if (detail.getEndTime().isAfter(detail.getStartTime())) {
                        Duration duration = Duration.between(detail.getStartTime(), detail.getEndTime());
                        long hours = duration.toHours();

                        // Xử lý giờ lẻ (làm tròn lên)
                        if (duration.toMinutesPart() > 0) {
                            hours++;
                        }

                        BigDecimal lineTotal = detail.getPrice().multiply(BigDecimal.valueOf(hours));
                        productAmount = productAmount.add(lineTotal);
                    }
                }
            }
        }

        if (invoice.getStartTime() != null && invoice.getEndTime() != null && invoice.getBilliardTable() != null) {
            // Validate endTime > startTime
            if (invoice.getEndTime().isAfter(invoice.getStartTime())) {
                serviceAmount = calculateServiceAmountByPriceList(
                        invoice.getStartTime(),
                        invoice.getEndTime(),
                        invoice.getBilliardTable().getTableType()
                );
            }
        }

        invoice.setProductAmount(productAmount);
        invoice.setServiceAmount(serviceAmount);

        BigDecimal subTotal = productAmount.add(serviceAmount);

        BigDecimal taxAmount = subTotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        invoice.setTaxAmount(taxAmount);

        BigDecimal totalBeforeDiscount = subTotal.add(taxAmount);

        BigDecimal discountAmount = BigDecimal.ZERO;
        if (voucher == null) {
            voucher = invoice.getVoucher();
        }

        if (voucher != null && voucher.getValue() != null) {
            discountAmount = calculateVoucherDiscount(voucher, totalBeforeDiscount);
        }

        BigDecimal totalAmount = totalBeforeDiscount.subtract(discountAmount);
        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }
        invoice.setTotalAmount(totalAmount);
    }

    /**
     * Tính số tiền giảm giá từ voucher
     */
    private BigDecimal calculateVoucherDiscount(Voucher voucher, BigDecimal totalAmount) {
        if (voucher.getType() == VoucherType.PERCENTAGE) {
            // Giảm theo phần trăm
            BigDecimal discount = totalAmount.multiply(voucher.getValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            // Kiểm tra giới hạn tối đa
            if (voucher.getMaximumValue() != null && discount.compareTo(voucher.getMaximumValue()) > 0) {
                discount = voucher.getMaximumValue();
            }
            return discount;
        } else if (voucher.getType() == VoucherType.CASH) {
            // Giảm số tiền cố định
            return voucher.getValue();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Tính serviceAmount dựa trên PriceList với các khung giờ khác nhau
     * @param startTime Thời gian bắt đầu
     * @param endTime Thời gian kết thúc
     * @param tableType Loại bàn
     * @return Tổng số tiền cần thanh toán cho dịch vụ
     */
    private BigDecimal calculateServiceAmountByPriceList(LocalDateTime startTime, LocalDateTime endTime,
                                                         TableType tableType) {
        List<PriceList> priceLists = pricelistRepository.findByTableType(tableType);

        if (priceLists.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Convert LocalDateTime to Date for compatibility with PriceList
        Date startDate = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());

        // Chuyển đổi sang milliseconds để dễ tính toán
        long currentTime = startDate.getTime();
        long endMillis = endDate.getTime();

        // Xử lý từng phút trong khoảng thời gian
        while (currentTime < endMillis) {
            Date currentDate = new Date(currentTime);
            boolean foundPriceList = false;
            BigDecimal hourlyPrice = BigDecimal.ZERO;

            // Tìm price list phù hợp với thời gian hiện tại
            for (PriceList priceList : priceLists) {
                if (isTimeInPriceRange(currentDate, priceList)) {
                    hourlyPrice = priceList.getUnitPrice();
                    foundPriceList = true;
                    break;
                }
            }

            if (!foundPriceList) {
                // Nếu không có price list phù hợp, dùng giá mặc định hoặc throw exception
                // Ở đây tôi sẽ dùng giá thấp nhất trong danh sách
                hourlyPrice = priceLists.stream()
                        .map(PriceList::getUnitPrice)
                        .min(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO);
            }

            // Tính thời gian đến cuối khung giờ hiện tại hoặc đến endTime
            long endOfCurrentPrice = findEndOfCurrentPriceRange(currentDate, priceLists);
            long nextTransitionTime = Math.min(endOfCurrentPrice, endMillis);

            // Tính số giờ/phút và cộng vào tổng
            long durationMillis = nextTransitionTime - currentTime;
            double durationHours = (double) durationMillis / (1000 * 60 * 60);

            BigDecimal amountForPeriod = hourlyPrice.multiply(BigDecimal.valueOf(durationHours));
            totalAmount = totalAmount.add(amountForPeriod);

            // Chuyển sang khoảng thời gian tiếp theo
            currentTime = nextTransitionTime;
        }

        return totalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Kiểm tra xem thời gian hiện tại có nằm trong price range không
     */
    private boolean isTimeInPriceRange(Date currentTime, PriceList priceList) {
        if (priceList.getStartTime() == null || priceList.getEndTime() == null) {
            return false;
        }

        // Lấy giờ và phút từ currentTime
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(currentTime);
        int currentHour = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(java.util.Calendar.MINUTE);
        int currentTotalMinutes = currentHour * 60 + currentMinute;

        // Lấy giờ và phút từ startTime
        cal.setTime(priceList.getStartTime());
        int startHour = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int startMinute = cal.get(java.util.Calendar.MINUTE);
        int startTotalMinutes = startHour * 60 + startMinute;

        // Lấy giờ và phút từ endTime
        cal.setTime(priceList.getEndTime());
        int endHour = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int endMinute = cal.get(java.util.Calendar.MINUTE);
        int endTotalMinutes = endHour * 60 + endMinute;

        // Xử lý case qua ngày (ví dụ: 22:00 - 6:00)
        if (endTotalMinutes < startTotalMinutes) {
            // Range qua ngày: current phải >= start hoặc <= end
            return currentTotalMinutes >= startTotalMinutes || currentTotalMinutes <= endTotalMinutes;
        } else {
            // Range thường trong ngày
            return currentTotalMinutes >= startTotalMinutes && currentTotalMinutes < endTotalMinutes;
        }
    }

    /**
     * Tìm thời điểm kết thúc của price range hiện tại
     */
    private long findEndOfCurrentPriceRange(Date currentTime, List<PriceList> priceLists) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(currentTime);

        // Tìm tất cả các price list và tìm thời điểm chuyển giao tiếp theo
        long nextTransition = Long.MAX_VALUE;

        for (PriceList priceList : priceLists) {
            if (priceList.getStartTime() != null && priceList.getEndTime() != null) {
                java.util.Calendar startCal = java.util.Calendar.getInstance();
                startCal.setTime(priceList.getStartTime());
                startCal.set(java.util.Calendar.YEAR, cal.get(java.util.Calendar.YEAR));
                startCal.set(java.util.Calendar.MONTH, cal.get(java.util.Calendar.MONTH));
                startCal.set(java.util.Calendar.DAY_OF_MONTH, cal.get(java.util.Calendar.DAY_OF_MONTH));

                java.util.Calendar endCal = java.util.Calendar.getInstance();
                endCal.setTime(priceList.getEndTime());
                endCal.set(java.util.Calendar.YEAR, cal.get(java.util.Calendar.YEAR));
                endCal.set(java.util.Calendar.MONTH, cal.get(java.util.Calendar.MONTH));
                endCal.set(java.util.Calendar.DAY_OF_MONTH, cal.get(java.util.Calendar.DAY_OF_MONTH));

                // Nếu endTime < startTime, nghĩa là range qua ngày
                if (endCal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + endCal.get(java.util.Calendar.MINUTE) <
                        startCal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + startCal.get(java.util.Calendar.MINUTE)) {
                    // Nếu currentTime nằm trong range qua ngày, endCal là ngày hôm sau
                    endCal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                }

                // Kiểm tra nếu startTime của price list > currentTime
                if (startCal.getTimeInMillis() > currentTime.getTime() && startCal.getTimeInMillis() < nextTransition) {
                    nextTransition = startCal.getTimeInMillis();
                }

                // Kiểm tra nếu endTime của price list > currentTime
                if (endCal.getTimeInMillis() > currentTime.getTime() && endCal.getTimeInMillis() < nextTransition) {
                    nextTransition = endCal.getTimeInMillis();
                }
            }
        }

        // Nếu không tìm thấy transition, trả về cuối ngày
        if (nextTransition == Long.MAX_VALUE) {
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            cal.set(java.util.Calendar.SECOND, 59);
            cal.set(java.util.Calendar.MILLISECOND, 999);
            nextTransition = cal.getTimeInMillis() + 1;
        }

        return nextTransition;
    }
}

package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.entities.invoice.Invoice;
import com.backend.billiards_management.entities.invoice.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> findByDeletedFalse();
    List<Invoice> findByStatusAndDeletedFalse(PaymentStatus status);
    List<Invoice> findByEmployeeAndDeletedFalse(Employee employee);

    List<Invoice> findByDeletedFalseAndCreatedAtBetween(Date startDate, Date endDate);
}

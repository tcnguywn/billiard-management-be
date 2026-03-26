package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.bank.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Integer> {
    Bank findByBankStatus(boolean bankStatus);
    Bank findByBankStatusAndDeletedFalse(boolean bankStatus);
    List<Bank> findByDeletedFalse();
}

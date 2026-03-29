package com.backend.billiards_management.services.bank;

import com.backend.billiards_management.dtos.request.bank.CreateBankReq;
import com.backend.billiards_management.dtos.request.bank.UpdateBankReq;
import com.backend.billiards_management.dtos.response.bank.BankRes;
import com.backend.billiards_management.entities.bank.Bank;
import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.BankRepository;
import com.backend.billiards_management.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    @Override
    @Transactional
    public BankRes createBank(CreateBankReq req) {
        Bank bank = Bank.builder()
                .bankBin(req.getBankBin())
                .bankAccountNo(req.getBankAccountNo())
                .bankAccountName(req.getBankAccountName())
                .bankStatus(false)
                .bankName(req.getBankName())
                .bankShortName(req.getBankShortName())
                .bankLogo(req.getBankLogo())
                .build();

        Bank savedBank = bankRepository.save(bank);
        return mapToRes(savedBank);
    }

    @Override
    @Transactional
    public BankRes updateBank(UpdateBankReq req) {
        Bank bank = bankRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find bank with id: " + req.getId()));

        if (bank.isDeleted()) {
            throw new AppException(ErrorCode.NOT_FOUND, "Bank with id " + req.getId() + " has been deleted");
        }

        if (req.getBankBin() != null) {
            bank.setBankBin(req.getBankBin());
        }
        if (req.getBankAccountNo() != null) {
            bank.setBankAccountNo(req.getBankAccountNo());
        }
        if (req.getBankAccountName() != null) {
            bank.setBankAccountName(req.getBankAccountName());
        }

        if (req.isBankStatus() && !bank.isBankStatus()) {
            deactivateAllBanks();
        }
        bank.setBankStatus(req.isBankStatus());

        Bank updatedBank = bankRepository.save(bank);
        return mapToRes(updatedBank);
    }

    @Override
    public BankRes getBankById(int id) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find bank with id: " + id));

        if (bank.isDeleted()) {
            throw new AppException(ErrorCode.NOT_FOUND, "Bank with id " + id + " has been deleted");
        }

        return mapToRes(bank);
    }

    @Override
    public List<BankRes> getAllBanks() {
        List<Bank> banks = bankRepository.findByDeletedFalse();
        List<BankRes> bankResList = new ArrayList<>();
        for (Bank bank : banks) {
            bankResList.add(mapToRes(bank));
        }
        return bankResList;
    }

    @Override
    @Transactional
    public void deleteBank(int id) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find bank with id: " + id));

        if (bank.isDeleted()) {
            throw new AppException(ErrorCode.NOT_FOUND, "Bank with id " + id + " has already been deleted");
        }

        bank.setDeleted(true);
        bank.setBankStatus(false);
        bankRepository.save(bank);
    }

    @Override
    public BankRes getActiveBank() {
        Bank bank = bankRepository.findByBankStatusAndDeletedFalse(true);
        if (bank == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "No active bank account found");
        }
        return mapToRes(bank);
    }

    private void deactivateAllBanks() {
        List<Bank> activeBanks = bankRepository.findByDeletedFalse();
        for (Bank b : activeBanks) {
            if (b.isBankStatus()) {
                b.setBankStatus(false);
                bankRepository.save(b);
            }
        }
    }

    private BankRes mapToRes(Bank bank) {
        return BankRes.builder()
                .bankAccountNo(bank.getBankAccountNo())
                .bankAccountName(bank.getBankAccountName())
                .bankStatus(bank.isBankStatus())
                .bankName(bank.getBankName())
                .bankShortName(bank.getBankShortName())
                .bankLogo(bank.getBankLogo())
                .build();
    }
}

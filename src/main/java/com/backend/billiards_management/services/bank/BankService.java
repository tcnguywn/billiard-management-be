package com.backend.billiards_management.services.bank;

import com.backend.billiards_management.dtos.request.bank.CreateBankReq;
import com.backend.billiards_management.dtos.request.bank.UpdateBankReq;
import com.backend.billiards_management.dtos.response.bank.BankRes;

import java.util.List;

public interface BankService {
    BankRes createBank(CreateBankReq req);
    BankRes updateBank(UpdateBankReq req);
    BankRes getBankById(int id);
    List<BankRes> getAllBanks();
    void deleteBank(int id);
    BankRes getActiveBank();
}

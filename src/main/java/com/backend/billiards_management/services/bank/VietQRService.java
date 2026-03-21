package com.backend.billiards_management.services.bank;

import com.backend.billiards_management.configuration.CloudinaryConfig;
import com.backend.billiards_management.configuration.RestTemplateConfig;
import com.backend.billiards_management.dtos.request.bank.VietQRRequest;
import com.backend.billiards_management.dtos.response.bank.DataObjectResponse;
import com.backend.billiards_management.dtos.response.bank.ShortVietQRRes;
import com.backend.billiards_management.dtos.response.bank.VietQRResponse;
import com.backend.billiards_management.entities.bank.Bank;
import com.backend.billiards_management.entities.invoice.Invoice;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.BankRepository;
import com.backend.billiards_management.repositories.InvoiceRepository;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.http.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VietQRService {

    private final RestTemplateConfig restTemplateConfig;
    private final InvoiceRepository invoiceRepository;
    private final BankRepository bankRepository;
    private final CloudinaryConfig cloudinaryConfig;
    private static final String VIETQR_URL = "https://api.vietqr.io/v2/generate";

    public DataObjectResponse generateVietQR(int id) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);
        if (invoiceOpt.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND, "Invoice not found");
        }

        Invoice invoice = invoiceOpt.get();
        Bank bank = bankRepository.findByBankStatus(true);
        if (bank == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Please create bank account first");
        }
        VietQRRequest req = new VietQRRequest();
        req.setAccountNo(bank.getBankAccountNo());
        req.setAccountName(bank.getBankAccountName());
        req.setAcqId(Integer.parseInt(bank.getBankBin()));
        req.setAmount(invoice.getTotalAmount());
        req.setAddInfo("Payment for invoice #" + id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VietQRRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<VietQRResponse> resEntity = restTemplateConfig.restTemplate().postForEntity(
                VIETQR_URL, entity, VietQRResponse.class);

        VietQRResponse response = resEntity.getBody();

        if (response != null && "00".equals(response.getCode()) && response.getData() != null) {
            return response.getData();
        }

        throw new AppException(ErrorCode.INTERNAL_ERROR, "Failed to generate VietQR code");
    }

    public ShortVietQRRes getShortVietQR(int id) {
        DataObjectResponse response = generateVietQR(id);

        try {
            Map uploadResult = cloudinaryConfig.cloudinary().uploader()
                    .upload(response.getQrDataURL(), ObjectUtils.emptyMap());

            String shortImageUrl = uploadResult.get("secure_url").toString();

            return new ShortVietQRRes(response.getQrCode(), shortImageUrl);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_ERROR, "Upload failed: " + e.getMessage());
        }
    }

}

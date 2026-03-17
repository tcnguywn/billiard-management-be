package com.backend.billiards_management.services.pricelist;

import com.backend.billiards_management.dtos.request.pricelist.PricelistReq;
import com.backend.billiards_management.dtos.request.pricelist.UpdatePricelistReq;
import com.backend.billiards_management.dtos.response.pricelist.PricelistRes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PriceListService {
    List<PricelistRes> getAllPriceLists();
    PricelistRes getPriceListById(Integer id);
    PricelistRes createPriceList(PricelistReq req);
    PricelistRes updatePriceList(UpdatePricelistReq req);
    void deletePriceList(Integer id);
}

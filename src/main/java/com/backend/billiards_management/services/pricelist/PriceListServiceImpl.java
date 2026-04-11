package com.backend.billiards_management.services.pricelist;

import com.backend.billiards_management.dtos.request.pricelist.PricelistReq;
import com.backend.billiards_management.dtos.request.pricelist.UpdatePricelistReq;
import com.backend.billiards_management.dtos.response.pricelist.PricelistRes;
import com.backend.billiards_management.entities.price_list.PriceList;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.PricelistRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceListServiceImpl implements PriceListService {
    private final ModelMapper modelMapper;

    private final PricelistRepository pricelistRepository;

    @Override
    public List<PricelistRes> getAllPriceLists() {
        return pricelistRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, PricelistRes.class))
                .toList();
    }

    @Override
    public PricelistRes getPriceListById(Integer id) {
        PriceList priceList = pricelistRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND ,"Cannot find price list with id: " + id)
        );
        return modelMapper.map(priceList, PricelistRes.class);
    }

    @Override
    public PricelistRes createPriceList(PricelistReq req) {
        return modelMapper.map(pricelistRepository.save(modelMapper.map(req, PriceList.class)), PricelistRes.class);
    }

    @Override
    public PricelistRes updatePriceList(UpdatePricelistReq req) {
        PriceList priceList = pricelistRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Cannot find price list with id: " + req.getId()));
        if(req.getStartTime() != null) {
            priceList.setStartTime(req.getStartTime());
        }
        if(req.getEndTime() != null) {
            priceList.setEndTime(req.getEndTime());
        }
        if(req.getUnitPrice() != null) {
            priceList.setUnitPrice(req.getUnitPrice());
        }
        if(req.getTableType() != null) {
            priceList.setTableType(req.getTableType());
        }
        return modelMapper.map(pricelistRepository.save(priceList), PricelistRes.class);
    }

    @Override
    public void deletePriceList(Integer id) {
        PriceList priceList = pricelistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Cannot find price list with id: " + id));
        pricelistRepository.delete(priceList);
    }
}

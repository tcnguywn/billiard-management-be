package com.backend.billiards_management.services.pricelist;

import com.backend.billiards_management.dtos.response.pricelist.PricelistRes;
import com.backend.billiards_management.entities.price_list.PriceList;
import com.backend.billiards_management.repositories.PricelistRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceListService {
    private final ModelMapper modelMapper;

    private final PricelistRepository pricelistRepository;

    public PricelistRes getPriceListById(Integer id) {
        PriceList priceList = pricelistRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cannot find price list with id: " + id)
        );
        return modelMapper.map(pricelistRepository.findById(id).orElse(null), PricelistRes.class);
    }
}

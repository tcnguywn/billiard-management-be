package com.backend.billiards_management.repositories;

import com.backend.billiards_management.dtos.constant.TableType;
import com.backend.billiards_management.dtos.request.pricelist.PricelistReq;
import com.backend.billiards_management.entities.price_list.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricelistRepository extends JpaRepository<PriceList, Integer> {
    PriceList findByPricelistName(String pricelistName);
    List<PriceList> findByTableType(TableType tableType);
}

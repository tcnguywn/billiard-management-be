package com.backend.billiards_management.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

//@UtilityClass
//public class PageableUtils {
//
//    public static Pageable sanitize(Pageable pageable, String defaultSortField, Set<String> validFields) {
//        List<Sort.Order> validOrders = pageable.getSort().stream()
//                .filter(order -> validFields.contains(order.getProperty()))
//                .toList();
//
//        Sort sort = validOrders.isEmpty()
//                ? Sort.by(Sort.Direction.DESC, defaultSortField)
//                : Sort.by(validOrders);
//
//        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
//    }
//}
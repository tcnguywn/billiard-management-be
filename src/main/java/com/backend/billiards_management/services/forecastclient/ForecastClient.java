package com.backend.billiards_management.services.forecastclient;

import com.backend.billiards_management.dtos.response.dashboard.RevenueData;
import com.backend.billiards_management.dtos.response.dashboard.enums.RevenueRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForecastClient {
    private final WebClient.Builder webClient;

    public List<RevenueData> getForecast(RevenueRange range) {
        try {
            List<RevenueData> result = webClient
//                    TODO: Sửa lại, để trong file Constant hoặc gì đó
//                    .baseUrl("http://localhost:8000/api/v1")
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forecast")
                            .queryParam("range", range)
                            .build())
                    .retrieve()
                    .bodyToFlux(RevenueData.class)
                    .collectList()
                    .block();

            return Optional.ofNullable(result)
                    .orElseGet(ArrayList::new);

//            TODO: Thêm exception chi tiết
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }

    }
}
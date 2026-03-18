package com.backend.billiards_management.services.dashboard;

import com.backend.billiards_management.dtos.response.dashboard.DashboardSummaryRes;
import com.backend.billiards_management.dtos.response.dashboard.RevenueData;
import com.backend.billiards_management.dtos.response.dashboard.RevenueRes;
import com.backend.billiards_management.dtos.response.dashboard.enums.RevenueRange;
import com.backend.billiards_management.dtos.response.dashboard.summary.GrowthSummary;
import com.backend.billiards_management.dtos.response.dashboard.summary.PlaytimeSummary;
import com.backend.billiards_management.dtos.response.dashboard.summary.RevenueSummary;
import com.backend.billiards_management.dtos.response.dashboard.summary.TableSummary;
import com.backend.billiards_management.repositories.DashboardRepository;
import com.backend.billiards_management.services.forecastclient.ForecastClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;
    private final ForecastClient forecastClient;

    @Override
    public DashboardSummaryRes getDashboardSummary() {

        RevenueSummary revenueSummary = dashboardRepository.getRevenueSummary();

        PlaytimeSummary playtimeSummary = dashboardRepository.getPlaytimeSummary();
//        PlaytimeSummary playtimeSummary = null;

        GrowthSummary growthSummary = dashboardRepository.getGrowthSummary();
//        GrowthSummary growthSummary = null;

        TableSummary tableSummary = dashboardRepository.getTableSummary();
//        TableSummary tableSummary = null;

        return DashboardSummaryRes.builder()
                .revenueSummary(revenueSummary)
                .playtimeSummary(playtimeSummary)
                .growthSummary(growthSummary)
                .tableSummary(tableSummary)
                .build();
    }

    @Override
    public RevenueRes getRevenuesByRange(RevenueRange range) {
        LocalDate today = LocalDate.now();

        LocalDate from;
        LocalDate to;

        List<RevenueData> actual;

        switch (range) {
            case WEEKLY -> {
                from = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                to = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(1);
                actual = dashboardRepository.getWeeklyRevenue(from, to);
            }
            case MONTHLY -> {
                from = today.withDayOfMonth(1);
                to = today.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);
                actual = dashboardRepository.getMonthlyRevenue(from, to);
            }
            case YEARLY -> {
                from = today.withDayOfYear(1);
                to = today.with(TemporalAdjusters.lastDayOfYear());
                actual = dashboardRepository.getYearlyRevenue(from, to);
            }
//            TODO: thêm exception chi tiết
            default -> throw new IllegalArgumentException("Invalid range");
        }

        List<RevenueData> predict = forecastClient.getForecast(range);

        return RevenueRes.builder()
                .actual(actual)
                .predict(predict)
                .build();
    }
}

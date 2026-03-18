package com.backend.billiards_management.services.dashboard;

import com.backend.billiards_management.dtos.response.dashboard.DashboardSummaryRes;
import com.backend.billiards_management.dtos.response.dashboard.RevenueRes;
import com.backend.billiards_management.dtos.response.dashboard.enums.RevenueRange;

public interface DashboardService {

    DashboardSummaryRes getDashboardSummary();

    RevenueRes getRevenuesByRange(RevenueRange range);
}

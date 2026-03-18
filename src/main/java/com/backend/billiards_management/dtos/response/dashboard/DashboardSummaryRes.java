package com.backend.billiards_management.dtos.response.dashboard;

import com.backend.billiards_management.dtos.response.dashboard.summary.GrowthSummary;
import com.backend.billiards_management.dtos.response.dashboard.summary.PlaytimeSummary;
import com.backend.billiards_management.dtos.response.dashboard.summary.RevenueSummary;
import com.backend.billiards_management.dtos.response.dashboard.summary.TableSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryRes {
    private RevenueSummary revenueSummary;

    private PlaytimeSummary playtimeSummary;

    private GrowthSummary growthSummary;

    private TableSummary tableSummary;
}

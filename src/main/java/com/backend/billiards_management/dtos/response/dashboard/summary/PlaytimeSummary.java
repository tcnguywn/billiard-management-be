package com.backend.billiards_management.dtos.response.dashboard.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaytimeSummary {
    private Double avgPlaytime;

    private Double lastWeekAvgPlaytime;

    private Double changePercentage;
}

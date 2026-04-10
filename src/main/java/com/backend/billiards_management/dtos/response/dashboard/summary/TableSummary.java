package com.backend.billiards_management.dtos.response.dashboard.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableSummary {
    private Long activeTables;

    private Long totalTables;

    private Double utilizationRate;
}

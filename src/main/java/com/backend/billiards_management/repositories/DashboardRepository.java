package com.backend.billiards_management.repositories;

import com.backend.billiards_management.dtos.response.dashboard.RevenueData;
import com.backend.billiards_management.dtos.response.dashboard.summary.GrowthSummary;
import com.backend.billiards_management.dtos.response.dashboard.summary.PlaytimeSummary;
import com.backend.billiards_management.dtos.response.dashboard.summary.RevenueSummary;
import com.backend.billiards_management.dtos.response.dashboard.summary.TableSummary;
import com.backend.billiards_management.entities.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

// Extend tạm Invoice để tạo Bean thì code mới chạy
// Đúng là phải tạo interface + implement
public interface DashboardRepository extends JpaRepository<Invoice, Long> {
//    COALESCE chuyển null -> 0
@Query(value = """
            WITH revenue AS (
                SELECT
                    COALESCE(SUM(total_amount) FILTER (
                        WHERE start_time >= date_trunc('day', CURRENT_TIMESTAMP)
                          AND start_time < date_trunc('day', CURRENT_TIMESTAMP) + INTERVAL '1 day'
                          AND status = 'PAID'
                    ), 0) AS today_revenue,
        
                    COALESCE(SUM(total_amount) FILTER (
                        WHERE start_time >= date_trunc('day', CURRENT_TIMESTAMP) - INTERVAL '7 day'
                          AND start_time < date_trunc('day', CURRENT_TIMESTAMP) - INTERVAL '6 day'
                          AND status = 'PAID'
                    ), 0) AS last_week_revenue
                FROM invoices
            )
            SELECT
                today_revenue AS "todayRevenue",
                last_week_revenue AS "lastWeekTodayRevenue",
                CASE
                    WHEN last_week_revenue = 0 THEN 0
                    ELSE ((today_revenue - last_week_revenue) / last_week_revenue) * 100
                END::float AS "changePercentage"
            FROM revenue
        """, nativeQuery = true)
RevenueSummary getRevenueSummary();

    @Query(value = """
                WITH current_week AS (
                    SELECT AVG(EXTRACT(EPOCH FROM (end_time - start_time))) /3600 AS avg_playtime
                    FROM invoices
                    WHERE status = 'PAID'
                      AND start_time >= date_trunc('week', CURRENT_DATE)
                      AND start_time < date_trunc('week', CURRENT_DATE) + INTERVAL '1 week'
                ),
                last_week AS (
                    SELECT AVG(EXTRACT(EPOCH FROM (end_time - start_time))) /3600 AS avg_playtime
                    FROM invoices
                    WHERE status = 'PAID'
                      AND start_time >= date_trunc('week', CURRENT_DATE) - INTERVAL '1 week'
                      AND start_time < date_trunc('week', CURRENT_DATE)
                )
                SELECT
                    cw.avg_playtime::float AS avgPlaytime,
                    lw.avg_playtime::float AS lastWeekAvgPlaytime,
                    CASE
                        WHEN lw.avg_playtime IS NULL OR lw.avg_playtime = 0 THEN NULL
                        ELSE ((cw.avg_playtime - lw.avg_playtime) / lw.avg_playtime) * 100
                    END::float AS changePercentage
                FROM current_week cw, last_week lw
            """, nativeQuery = true)
    PlaytimeSummary getPlaytimeSummary();

    @Query(value = """
                WITH current_week AS (
                    SELECT SUM(total_amount) AS revenue
                    FROM invoices
                    WHERE status = 'PAID'
                      AND start_time >= date_trunc('week', CURRENT_DATE)
                      AND start_time < date_trunc('week', CURRENT_DATE) + INTERVAL '1 week'
                ),
                last_week AS (
                    SELECT SUM(total_amount) AS revenue
                    FROM invoices
                    WHERE status = 'PAID'
                      AND start_time >= date_trunc('week', CURRENT_DATE) - INTERVAL '1 week'
                      AND start_time < date_trunc('week', CURRENT_DATE)
                )
                SELECT
                    CASE
                        WHEN lw.revenue IS NULL OR lw.revenue = 0 THEN NULL
                        ELSE ((cw.revenue - lw.revenue) / lw.revenue) * 100
                    END::float AS growthRate
                FROM current_week cw, last_week lw
            """, nativeQuery = true)
    GrowthSummary getGrowthSummary();

    @Query(value = """
                SELECT
                    COUNT(*) FILTER (WHERE status = 'IN_USE') AS activeTables,
                    COUNT(*) AS totalTables,
                    CASE
                        WHEN COUNT(*) = 0 THEN 0
                        ELSE (COUNT(*) FILTER (WHERE status = 'IN_USE')::float / COUNT(*)) * 100
                    END AS utilizationRate
                FROM billiard_tables
            """, nativeQuery = true)
    TableSummary getTableSummary();


    @Query(value = """
            SELECT TO_CHAR(DATE(start_time), 'YYYY-MM-DD') AS "dateLabel",
                   SUM(total_amount) AS revenue
            FROM invoices
            WHERE status = 'PAID'
              AND start_time >= :from
              AND start_time < :to
            GROUP BY DATE(start_time)
            ORDER BY DATE(start_time)
            """, nativeQuery = true)
    List<RevenueData> getWeeklyRevenue(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query(value = """
            SELECT ('Week ' || (((EXTRACT(DAY FROM start_time) - 1) / 7)::int + 1)) AS "dateLabel",
                    SUM(total_amount) AS revenue
            FROM invoices
            WHERE status = 'PAID'
              AND start_time >= :from
              AND start_time < :to
            GROUP BY "dateLabel"
            ORDER BY MIN(start_time)
            """, nativeQuery = true)
    List<RevenueData> getMonthlyRevenue(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query(value = """
            SELECT ('Month ' || EXTRACT(MONTH FROM start_time)) AS "dateLabel",
                    SUM(total_amount) AS revenue
            FROM invoices
            WHERE status = 'PAID'
              AND start_time >= :from
              AND start_time < :to
            GROUP BY "dateLabel"
            ORDER BY MIN(start_time)
            """, nativeQuery = true)
    List<RevenueData> getYearlyRevenue(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}

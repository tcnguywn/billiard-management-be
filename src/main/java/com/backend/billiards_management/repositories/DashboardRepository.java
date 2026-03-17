package com.backend.billiards_management.repositories;

import com.backend.billiards_management.dtos.response.dashboard.RevenueData;
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
//    @Query(value = """
//        WITH revenue AS (
//            SELECT
//                COALESCE(SUM(CASE
//                    WHEN created_at >= CURRENT_DATE
//                    AND created_at < CURRENT_DATE + INTERVAL '1 day'
//                    THEN total_amount END),0) AS today_revenue,
//
//                COALESCE(SUM(CASE
//                    WHEN created_at >= CURRENT_DATE - INTERVAL '7 day'
//                    AND created_at < CURRENT_DATE - INTERVAL '6 day'
//                    THEN total_amount END),0) AS last_week_revenue
//            FROM invoices
//        )
//        SELECT
//            today_revenue AS "todayRevenue",
//            last_week_revenue AS "lastWeekRevenue",
//            CASE
//                WHEN last_week_revenue = 0 THEN 0
//                ELSE ((today_revenue - last_week_revenue) / last_week_revenue) * 100
//            END AS "changePercentage"
//        FROM revenue
//    """, nativeQuery = true)
//    RevenueSummary getRevenueSummary();

//    @Query(value = """
//
//    """, nativeQuery = true)
//    PlaytimeSummary getPlaytimeSummary();
//
//    @Query(value = """
//
//    """, nativeQuery = true)
//    GrowthSummary getGrowthSummary();
//
//    @Query(value = """
//
//    """, nativeQuery = true)
//    TableSummary getTableSummary();


    @Query(value = """
            SELECT TO_CHAR(DATE(created_at), 'YYYY-MM-DD') AS "dateLabel",
                   SUM(total_amount) AS revenue
            FROM invoices
            WHERE created_at >= :from
              AND created_at < :to
            GROUP BY DATE(created_at)
            ORDER BY DATE(created_at)
            """, nativeQuery = true)
    List<RevenueData> getWeeklyRevenue(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query(value = """
            SELECT ('Week ' || (((EXTRACT(DAY FROM created_at) - 1) / 7)::int + 1)) AS "dateLabel",
                    SUM(total_amount) AS revenue
            FROM invoices
            WHERE created_at >= :from
              AND created_at < :to
            GROUP BY "dateLabel"
            ORDER BY MIN(created_at)
            """, nativeQuery = true)
    List<RevenueData> getMonthlyRevenue(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query(value = """
            SELECT ('Month ' || EXTRACT(MONTH FROM created_at)) AS "dateLabel",
                    SUM(total_amount) AS revenue
            FROM invoices
            WHERE created_at >= :from
              AND created_at < :to
            GROUP BY "dateLabel"
            ORDER BY MIN(created_at)
            """, nativeQuery = true)
    List<RevenueData> getYearlyRevenue(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}

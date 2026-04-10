package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.order_detail.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query(value = """
                SELECT p.id AS "productId",
                        p.name AS "productName",
                        pc.category_name AS "categoryName",
                        SUM(o.quantity) AS "totalSold",
                        SUM(o.quantity * o.price) AS "totalRevenue"
                FROM order_details o
                JOIN products p ON o.product_id = p.id
                JOIN product_categories pc ON p.category_id = pc.id
                WHERE pc.type = 'RETAIL'
                    AND o.start_time BETWEEN :startDate AND :endDate
                GROUP BY p.id, p.name, pc.category_name
            """, nativeQuery = true)
    List<TopProductRes> getOrderDetailsByRange(LocalDate startDate, LocalDate endDate);
}

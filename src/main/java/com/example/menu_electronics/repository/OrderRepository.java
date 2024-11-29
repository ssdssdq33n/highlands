package com.example.menu_electronics.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.menu_electronics.entity.Order;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatusNot(String status);

    //    @Query(value = "SELECT DATE(STR_TO_DATE(o.order_done, '%d/%m/%Y %H:%i:%s')) AS date, SUM(o.total_amount) AS
    // totalSales " +
    //            "FROM `order` o " +
    //            "WHERE o.order_done IS NOT NULL " +
    //            "AND STR_TO_DATE(o.order_done, '%d/%m/%Y %H:%i:%s') " +
    //            "BETWEEN STR_TO_DATE(:startDate, '%d/%m/%Y %H:%i:%s') " +
    //            "AND STR_TO_DATE(:endDate, '%d/%m/%Y %H:%i:%s') " +
    //            "GROUP BY date " +
    //            "ORDER BY date DESC", nativeQuery = true)
    //    List<Object[]> findDailyTotalSalesForPeriod(@Param("startDate") String startDate,
    //                                                @Param("endDate") String endDate);

    @Query(
            value = "SELECT DATE(STR_TO_DATE(o.order_done, '%d/%m/%Y %H:%i:%s')) AS date, "
                    + "SUM(o.total_amount) AS totalSales, "
                    + "COUNT(oi.id) AS totalOrderItems "
                    + // Đếm tổng số lượng orderItems
                    "FROM `order` o "
                    + "JOIN order_item oi ON o.id = oi.order_id "
                    + // Giả định tên bảng là order_item và order_id là khóa ngoại
                    "WHERE STR_TO_DATE(o.order_done, '%d/%m/%Y %H:%i:%s') BETWEEN STR_TO_DATE(:startDate, '%d/%m/%Y %H:%i:%s') "
                    + "AND STR_TO_DATE(:endDate, '%d/%m/%Y %H:%i:%s') "
                    + "GROUP BY date "
                    + "ORDER BY date DESC",
            nativeQuery = true)
    List<Object[]> findDailyTotalSalesAndOrderItems(
            @Param("startDate") String startDate, @Param("endDate") String endDate);

    Order findByIdAndStatus(Long orderId, String status);

    Optional<Order> findByUserIdAndStatus(String userId, String status);
}

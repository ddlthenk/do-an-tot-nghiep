package com.datn.commonbase.repository;

import com.datn.commonbase.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByOrderStatus(int status, Pageable pageable);

    Page<Order> findAllByCustomerNameContaining(String searchValue, Pageable pageable);

    Page<Order> findAllByUserId(Long userId, Pageable pageable);

    Page<Order> findAllByUserIdAndOrderStatus(Long userId, int status, Pageable pageable);

    Long countAllBy();

    Long countAllByOrderStatus(int status);

    @Query(value = "SELECT COUNT(*) FROM tbl_order WHERE YEAR(order_date) = YEAR(?1) AND MONTH(order_date) = MONTH(?1) AND DAY(order_date) = DAY(?1)", nativeQuery = true)
    Long countBySpecificOrderDate(Date orderDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate = ?1")
    Long countByExactOrderDate(Date orderDate);

    @Query(value = "SELECT COUNT(*) FROM tbl_order WHERE YEAR(order_date) = YEAR(?1) AND MONTH(order_date) = MONTH(?1)", nativeQuery = true)
    Long countByMonthAndYear(Date orderDate);

    @Query(value = "SELECT SUM(tbl_order.sub_total) FROM tbl_order WHERE YEAR(order_date) = YEAR(?1) AND MONTH(order_date) = MONTH(?1) AND order_status = 6", nativeQuery = true)
    Long sumByMonthAndYear(Date orderDate);

    @Query(value = "SELECT * FROM tbl_order WHERE YEAR(order_date) = YEAR(?1) AND MONTH(order_date) = MONTH(?1) AND order_status = 6", nativeQuery = true)
    List<Order> getListOrderByMonthAndYear(Date orderDate);

    @Query(value = "SELECT SUM(tbl_order.sub_total) FROM tbl_order where tbl_order.order_status = 6", nativeQuery = true)
    Long sumAll();

    @Query(value = "SELECT COUNT(*) FROM tbl_order WHERE order_date BETWEEN ?1 AND ?2 and order_status = 6", nativeQuery = true)
    Long countBetweenDate(Date orderDate1, Date orderDate2);
}

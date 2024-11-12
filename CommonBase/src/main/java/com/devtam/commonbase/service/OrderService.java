package com.devtam.commonbase.service;

import com.devtam.commonbase.entity.Order;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {
    public Order getOrderById(long orderId);

    public List<Order> getOrderList(int limit, int page);

    public List<Order> getOrderList(int limit, int page, int status);

    public Page<Order> getOrderPage(int page, int limit);

    List<Long> getLast7dayTotalSales();

    List<Long> getLast12MonthTotalSales();

    Long getTotalOrder();

    Long getTotalPriceOrder();

    List<Long> getListPriceOrder();

    Long getTotalDoneOrder();

    public Page<Order> getOrderPageByUser(long userId, int page, int limit);

    public Page<Order> getOrderPageByUserAndStatus(long userId, int page, int limit, int status);

    public Page<Order> getOrderPage(int page, int limit, int status);

    public Page<Order> searchOrderPage(String searchValue, int page, int limit);

    public Order saveOrder(Order order);

    public boolean deleteOrder(Order order);

    public boolean deleteOrder(long orderId);
}

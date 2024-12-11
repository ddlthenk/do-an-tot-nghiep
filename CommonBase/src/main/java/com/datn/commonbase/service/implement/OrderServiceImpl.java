package com.datn.commonbase.service.implement;

import com.datn.commonbase.constant.PaymentStatus;
import com.datn.commonbase.entity.Order;
import com.datn.commonbase.repository.OrderRepository;
import com.datn.commonbase.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    private final Logger _log = LogManager.getLogger(OrderServiceImpl.class);

    @Override
    public Order getOrderById(long orderId) {
        return orderRepository.findById(orderId).get();
    }

    @Override
    public List<Order> getOrderList(int page, int limit) {
        try {
            List<Order> orderList = orderRepository.findAll(PageRequest.of(page, limit)).getContent();
            return orderList;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Order> getOrderList(int page, int limit, int status) {
        try {
            List<Order> orderList = orderRepository.findAllByOrderStatus(status, PageRequest.of(page, limit)).getContent();
            return orderList;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Page<Order> getOrderPage(int page, int limit) {
        try {
            Page<Order> orderPage = orderRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "orderId")));
            return orderPage;
        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public List<Long> getLast7dayTotalSales() {
        List<Long> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        try {
            for (int i = 0; i < 12; i++) {
                LocalDate dateCount = today.minusMonths(i);
                Date sqlDate = Date.valueOf(dateCount);
                Long count = orderRepository.countByMonthAndYear(sqlDate);
                if (count != null) {
                    result.add(count);
                } else {
                    result.add(0L);
                }
            }
            return result;
        } catch (Exception e) {
            _log.error(e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Long> getLast12MonthTotalSales() {
        List<Long> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        try {
            for (int i = 11; i >= 0; i--) {
                LocalDate dateCount = today.minusMonths(i);
                Date sqlDate = Date.valueOf(dateCount);
                Long count = orderRepository.countByMonthAndYear(sqlDate);
                if (count != null) {
                    result.add(count);
                } else {
                    result.add(0L);
                }
            }
            return result;
        } catch (Exception e) {
            _log.error(e);
            return Collections.emptyList();
        }
    }

    @Override
    public Long getTotalOrder() {
        return orderRepository.countAllBy();
    }

    @Override
    public Long getTotalPriceOrder() {
        return orderRepository.sumAll();
    }

    @Override
    public Long getTotalProfits() {
        return getTotalPriceOrder() * 40 / 100;
    }

    @Override
    public List<Long> getListPriceOrder() {
        List<Long> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        try {
            for (int i = 11; i >= 0; i--) {
                LocalDate dateCount = today.minusMonths(i);
                Date sqlDate = Date.valueOf(dateCount);
                Long count = orderRepository.sumByMonthAndYear(sqlDate);
                if (count != null) {
                    result.add(count);
                } else {
                    result.add(0L);
                }
            }
            return result;
        } catch (Exception e) {
            _log.error(e);
            return Collections.emptyList();
        }
    }

//    public Map<String, Object> getTotalPriceOrderMap() {
//        Map<String, Object> result = new HashMap<>();
//        LocalDate today = LocalDate.now();
//        List<Long> totalPrices = new ArrayList<>();
//        try {
//            for (int i = 11; i >= 0; i--) {
//                LocalDate dateCount = today.minusMonths(i);
//                Date sqlDate = Date.valueOf(dateCount);
//                List<Order> orderList = orderRepository.getListOrderByMonthAndYear(sqlDate);
//                int count = 0;
//                for (Order order : orderList) {
//                    order.getSubTotal();
//                    count += order.getTotal();
//                }
//                if (orderList != null) {
//                    totalPrices.add(count);
//                } else {
//                    totalPrices.add(0L);
//                }
//            }
//            return result;
//        } catch (Exception e) {
//            _log.error(e);
//            return Collections.emptyList();
//        }
//    }

    public List<Long> getListProfits() {
        List<Long> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        try {
            for (int i = 11; i >= 0; i--) {
                LocalDate dateCount = today.minusMonths(i);
                Date sqlDate = Date.valueOf(dateCount);
                Long count = orderRepository.sumByMonthAndYear(sqlDate);
                if (count != null) {
                    result.add(count * 40 / 100);
                } else {
                    result.add(0L);
                }
            }
            return result;
        } catch (Exception e) {
            _log.error(e);
            return Collections.emptyList();
        }
    }

    @Override
    public Long getTotalDoneOrder() {
        return orderRepository.countAllByOrderStatus(PaymentStatus.DONE.getValue());
    }

    @Override
    public Page<Order> getOrderPageByUser(long userId, int page, int limit) {
        try {
            Page<Order> orderPage = orderRepository.findAllByUserId(userId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "orderId")));
            return orderPage;
        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public Page<Order> getOrderPageByUserAndStatus(long userId, int page, int limit, int status) {
        try {
            Page<Order> orderPage = orderRepository.findAllByUserIdAndOrderStatus(userId, status, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "orderId")));
            return orderPage;
        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public Page<Order> getOrderPage(int page, int limit, int status) {
        try {
            Page<Order> orderPage = orderRepository.findAllByOrderStatus(status, PageRequest.of(page, limit));
            return orderPage;
        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public Page<Order> searchOrderPage(String searchValue, int page, int limit) {
        try {
            Page<Order> orderPage = orderRepository.findAllByCustomerNameContaining(searchValue, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "orderDate")));
            return orderPage;
        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public boolean deleteOrder(Order order) {
        try {
            orderRepository.delete(order);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteOrder(long orderId) {
        try {
            orderRepository.deleteById(orderId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

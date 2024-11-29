package com.example.menu_electronics.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.menu_electronics.dto.request.OrderUpdateRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.menu_electronics.dto.request.OrderRequest;
import com.example.menu_electronics.dto.response.ChartDashboard;
import com.example.menu_electronics.dto.response.ChartOrderResponse;
import com.example.menu_electronics.dto.response.OrderItemResponse;
import com.example.menu_electronics.dto.response.OrderResponse;
import com.example.menu_electronics.entity.Order;
import com.example.menu_electronics.entity.OrderItem;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.mapper.OrderItemMapper;
import com.example.menu_electronics.mapper.OrderMapper;
import com.example.menu_electronics.repository.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    FeedBackRepository feedBackRepository;
    OrderItemRepository orderItemRepository;
    OrderMapper orderMapper;
    OrderItemMapper orderItemMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi thêm mới
    public Long createOrder(String userId) {
        Optional<Order> orderCheck = orderRepository.findByUserIdAndStatus(userId,"PENDING");
        if(orderCheck.isPresent()) return orderCheck.get().getId();
        Order order = new Order();
        order.setStatus("PENDING");
        order.setUserId(userId);
        order.setAddress("");
        order.setPhone("");
        order.setTotalAmount(BigDecimal.ZERO);
        order.setTotalPrice(BigDecimal.ZERO);
        order.setPayment(false);
        order.setTypePayment("Payment upon receipt");
        order = orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi thêm mới
    public OrderResponse updateOrder(OrderRequest orderRequest, Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        List<OrderItem> orderItems = orderItemRepository.findAllById(orderRequest.getOrderItemIds());
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        order.setOrderDate(orderTime.format(formatter));
        order.setStatus("PENDING");
        order.setTotalAmount(totalAmount);
        order.setTotalPrice(totalAmount);
        order.setOrderItems(orderItems);
        order = orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    //    @Cacheable(value = "orders", key = "#id")
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findByIdAndStatus(id, "PENDING");
        if (order != null) {
            OrderResponse orderResponse = orderMapper.toOrderResponse(order);
            List<OrderItemResponse> orderItemResponses = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                OrderItemResponse orderItemResponse = orderItemMapper.toOrderItemResponse(orderItem);
                orderItemResponse.setOrderId(order.getId());
                orderItemResponse.setNameProduct(orderItem.getProduct().getName());
                orderItemResponse.setImageUrl(orderItem.getProduct().getImage());
                orderItemResponse.setProductId(orderItem.getProduct().getId());
                orderItemResponse.setSize(orderItem.getSize());
                orderItemResponses.add(orderItemResponse);
            }
            orderResponse.setOrderItemResponses(orderItemResponses);
            return orderResponse;
        }
        return null;
    }

    public OrderResponse getOrderProcessing(Long id) {
        Order order = orderRepository.findByIdAndStatus(id, "PROCESSING");
        if (order != null) {
            OrderResponse orderResponse = orderMapper.toOrderResponse(order);
            List<OrderItemResponse> orderItemResponses = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                OrderItemResponse orderItemResponse = orderItemMapper.toOrderItemResponse(orderItem);
                orderItemResponse.setOrderId(order.getId());
                orderItemResponse.setNameProduct(orderItem.getProduct().getName());
                orderItemResponse.setImageUrl(orderItem.getProduct().getImage());
                orderItemResponse.setProductId(orderItem.getProduct().getId());
                orderItemResponse.setSize(orderItem.getSize());
                orderItemResponses.add(orderItemResponse);
            }
            orderResponse.setOrderItemResponses(orderItemResponses);
            return orderResponse;
        }
        return null;
    }

    public OrderResponse getOrderId(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            OrderResponse orderResponse = orderMapper.toOrderResponse(order.orElse(null));
            List<OrderItemResponse> orderItemResponses = new ArrayList<>();
            for (OrderItem orderItem : order.get().getOrderItems()) {
                OrderItemResponse orderItemResponse = orderItemMapper.toOrderItemResponse(orderItem);
                orderItemResponse.setOrderId(order.get().getId());
                orderItemResponse.setNameProduct(orderItem.getProduct().getName());
                orderItemResponse.setImageUrl(orderItem.getProduct().getImage());
                orderItemResponse.setProductId(orderItem.getProduct().getId());
                orderItemResponse.setSize(orderItem.getSize());
                orderItemResponses.add(orderItemResponse);
            }
            orderResponse.setOrderItemResponses(orderItemResponses);
            return orderResponse;
        }
        return null;
    }

    //    @Cacheable(value = "allOrders")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItemResponse> orderItemResponses = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                OrderItemResponse orderItemResponse = orderItemMapper.toOrderItemResponse(orderItem);
                orderItemResponse.setOrderId(order.getId());
                orderItemResponse.setNameProduct(orderItem.getProduct().getName());
                orderItemResponse.setImageUrl(orderItem.getProduct().getImage());
                orderItemResponse.setProductId(orderItem.getProduct().getId());
                orderItemResponse.setSize(orderItem.getSize());
                orderItemResponses.add(orderItemResponse);
            }
            OrderResponse orderResponse = orderMapper.toOrderResponse(order);
            orderResponse.setOrderItemResponses(orderItemResponses);
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi thêm mới
    public String deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        //        order.setStatus("CANCELLED");
        //        orderRepository.save(order);
        orderRepository.delete(order);
        return "Deleted Successfully";
    }

    public String setTypePaymentOrder(String type, Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        order.setTypePayment(type);
        orderRepository.save(order);
        return "Set Type Payment Order Successfully";
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi thêm mới
    public String sendOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        if (!order.getStatus().equals("PENDING")) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemResponse orderItemResponse = orderItemMapper.toOrderItemResponse(orderItem);
            orderItemResponse.setOrderId(order.getId());
            orderItemResponses.add(orderItemResponse);
        }
        orderResponse.setOrderItemResponses(orderItemResponses);
        order.setStatus("PROCESSING");
        order.setPayment(false);
        order.setOrderDate(orderTime.format(formatter));
        orderRepository.save(order);
        kafkaTemplate.send("notification-order", orderResponse);
        return "Send Order Successfully";
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi thêm mới
    public String doneOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        if (!order.getStatus().equals("PROCESSING")) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        if (order.getOrderDate() == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemResponse orderItemResponse = orderItemMapper.toOrderItemResponse(orderItem);
            orderItemResponse.setOrderId(order.getId());
            orderItemResponses.add(orderItemResponse);
        }
        orderResponse.setOrderItemResponses(orderItemResponses);
        orderResponse.setStatus("DONE");
        order.setStatus("DONE");
        order.setPayment(false);
        order.setOrderDone(orderTime.format(formatter));
        orderRepository.save(order);
        kafkaTemplate.send("notification-order", orderResponse);
        return "Send Order Successfully";
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi thêm mới
    public String shipOrder(OrderUpdateRequest request) {
        Order order = orderRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        if (!order.getStatus().equals("PROCESSING")) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        if (order.getOrderDate() == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemResponse orderItemResponse = orderItemMapper.toOrderItemResponse(orderItem);
            orderItemResponse.setOrderId(order.getId());
            orderItemResponses.add(orderItemResponse);
        }
        orderResponse.setOrderItemResponses(orderItemResponses);
        orderResponse.setStatus("DONE");
        order.setStatus("DONE");
        order.setOrderDone(orderTime.format(formatter));
        order.setPhone(request.getPhone());
        order.setAddress(request.getAddress());
        order.setPayment(request.getPayment());
        order.setName(request.getName());
        order.setEmail(request.getEmail());
        order.setTotalPrice(request.getTotalPrice());
        order.setDescription(request.getDescription());
        order.setTypePayment(request.getTypePayment());
        orderRepository.save(order);
        kafkaTemplate.send("notification-order", orderResponse);
        return "Ship Order Successfully";
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi thêm mới
    public List<ChartOrderResponse> getDailyTotalSalesForLast7Days(String startTime, String endTime) {
        //        LocalDateTime now = LocalDateTime.now();
        //        LocalDateTime sevenDaysAgo = now.minusDays(7);
        startTime = startTime + " 00:00:00";
        endTime = endTime + " 23:59:59";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);

        Random random = new Random();

        //        // Thiết lập startTime là đầu giờ và endTime là cuối giờ
        //        LocalDateTime start = startDate.atStartOfDay(); // 00:00:00
        //        LocalDateTime end = endDate.atTime(23, 59, 59); // 23:59:59

        log.info("start: {}", start.format(formatter));
        log.info("end: {}", end.format(formatter));

        List<Object[]> results =
                orderRepository.findDailyTotalSalesAndOrderItems(start.format(formatter), end.format(formatter));
        List<ChartOrderResponse> list = results.stream()
                .map(row -> new ChartOrderResponse(
                        (BigDecimal) row[1],
                        row[0].toString(),
                        (Long) row[2],
                        "IPOS" + String.valueOf(10000000 + random.nextInt(90000000))))
                .collect(Collectors.toList());

        return list;
    }

    public ChartDashboard getTotalDashboard() {
        ChartDashboard chartDashboard = new ChartDashboard();
        chartDashboard.setOrderLength(orderRepository.count());
        chartDashboard.setFeedbackLength(feedBackRepository.count());
        chartDashboard.setCategoryLength(categoryRepository.count());
        chartDashboard.setProductLength(productRepository.count());
        return chartDashboard;
    }
}

package com.example.menu_electronics.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.menu_electronics.dto.request.OrderItemRequest;
import com.example.menu_electronics.dto.response.OrderItemResponse;
import com.example.menu_electronics.entity.Order;
import com.example.menu_electronics.entity.OrderItem;
import com.example.menu_electronics.entity.Product;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.mapper.OrderItemMapper;
import com.example.menu_electronics.repository.OrderItemRepository;
import com.example.menu_electronics.repository.OrderRepository;
import com.example.menu_electronics.repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemService {
    OrderItemRepository orderItemRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;
    OrderItemMapper orderItemMapper;

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi thêm mới
    public OrderItemResponse createOrderItem(OrderItemRequest orderItemRequest) {
        Order order = orderRepository
                .findById(orderItemRequest.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        Product product = productRepository
                .findById(orderItemRequest.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Optional<OrderItem> orderItemOld = orderItemRepository.findByProductIdAndOrder_IdAndSize(
                orderItemRequest.getProductId(), orderItemRequest.getOrderId(), orderItemRequest.getSize());
        if (orderItemOld.isPresent()) {
            orderItemOld.get().setQuantity(orderItemOld.get().getQuantity() + orderItemRequest.getQuantity());
            orderItemOld.get().setDescription(orderItemRequest.getDescription());
            orderItemOld.get().setSize(orderItemRequest.getSize());
            OrderItem orderItemSuccess = orderItemRepository.save(orderItemOld.get());
            order.setTotalAmount(order.getTotalAmount()
                    .add(orderItemSuccess.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity()))));
            order.setTotalPrice(order.getTotalAmount()
                    .add(orderItemSuccess.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity()))));
            orderRepository.save(order);
            OrderItemResponse response = orderItemMapper.toOrderItemResponse(orderItemSuccess);
            response.setOrderId(order.getId());
            response.setImageUrl(orderItemSuccess.getProduct().getImage());
            response.setNameProduct(orderItemSuccess.getProduct().getName());
            response.setProductId(orderItemSuccess.getProduct().getId());
            return response;
        }
        if (orderItemRequest.getQuantity() < 1) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        OrderItem orderItem = orderItemMapper.toOrderItem(orderItemRequest);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPrice(product.getPrice());
        orderItem = orderItemRepository.save(orderItem);

        order.setTotalAmount(
                order.getTotalAmount().add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
        order.setTotalPrice( order.getTotalAmount().add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
        orderRepository.save(order);
        OrderItemResponse response = orderItemMapper.toOrderItemResponse(orderItem);
        response.setOrderId(order.getId());
        response.setImageUrl(orderItem.getProduct().getImage());
        response.setNameProduct(orderItem.getProduct().getName());
        response.setProductId(orderItem.getProduct().getId());
        return response;
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi cập nhật
    public OrderItemResponse updateOrderItem(OrderItemRequest orderItemRequest) {
        Order order = orderRepository
                .findById(orderItemRequest.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
        Product product = productRepository
                .findById(orderItemRequest.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Optional<OrderItem> orderItemOld = orderItemRepository.findByProductIdAndOrder_IdAndSize(
                orderItemRequest.getProductId(), orderItemRequest.getOrderId(), orderItemRequest.getSize());
        if (orderItemOld.isPresent()) {
            OrderItem existingOrderItem = orderItemOld.get();
            // Trừ số tiền của orderItem hiện có
            BigDecimal previousAmount =
                    existingOrderItem.getPrice().multiply(BigDecimal.valueOf(existingOrderItem.getQuantity()));
            order.setTotalAmount(order.getTotalAmount().subtract(previousAmount));
            order.setTotalPrice(order.getTotalAmount().subtract(previousAmount));
            order = orderRepository.save(order);
            orderItemOld.get().setQuantity(orderItemRequest.getQuantity());
            orderItemOld.get().setDescription(orderItemRequest.getDescription());
            orderItemOld.get().setSize(orderItemRequest.getSize());
            OrderItem orderItemSuccess = orderItemRepository.save(orderItemOld.get());
            order.setTotalAmount(order.getTotalAmount()
                    .add(orderItemSuccess.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity()))));
            order.setTotalPrice(order.getTotalAmount()
                    .add(orderItemSuccess.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity()))));
            orderRepository.save(order);
            OrderItemResponse response = orderItemMapper.toOrderItemResponse(orderItemSuccess);
            response.setOrderId(order.getId());
            response.setImageUrl(orderItemSuccess.getProduct().getImage());
            response.setNameProduct(orderItemSuccess.getProduct().getName());
            response.setProductId(orderItemSuccess.getProduct().getId());
            return response;
        }
        if (orderItemRequest.getQuantity() < 1) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        OrderItem orderItem = orderItemMapper.toOrderItem(orderItemRequest);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPrice(product.getPrice());
        orderItem = orderItemRepository.save(orderItem);

        order.setTotalAmount(
                order.getTotalAmount().add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
        order.setTotalPrice(
                order.getTotalAmount().add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
        orderRepository.save(order);
        OrderItemResponse response = orderItemMapper.toOrderItemResponse(orderItem);
        response.setOrderId(order.getId());
        response.setImageUrl(orderItem.getProduct().getImage());
        response.setNameProduct(orderItem.getProduct().getName());
        response.setProductId(orderItem.getProduct().getId());
        return response;
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi xóa
    public String deleteOrderItem(Long id) {
        OrderItem orderItem =
                orderItemRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDERITEM_NOT_EXISTED));
        Order order = orderItem.getOrder();
        // Giảm tổng số tiền khi xóa OrderItem
        order.setTotalAmount(order.getTotalAmount()
                .subtract(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
        order.setTotalPrice(order.getTotalAmount()
                .subtract(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
        orderRepository.save(order);
        orderItemRepository.delete(orderItem);
        return "Deleted successfully";
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true) // Xóa cache khi xóa
    public String deleteAllOrderItem(List<Long> ids) {
        List<OrderItem> orderItems = orderItemRepository.findAllById(ids);
        Order order = orderItems.get(0).getOrder();
        // Giảm tổng số tiền khi xóa OrderItem
        order.setTotalAmount(BigDecimal.valueOf(0));
        order.setTotalPrice(BigDecimal.valueOf(0));
        orderRepository.save(order);
        orderItemRepository.deleteAll(orderItems);
        return "Deleted successfully";
    }

    //    @Cacheable(value = "orderItems", key = "#id") // Cache kết quả của phương thức getOrderItemById
    public OrderItemResponse getOrderItemById(Long id) {
        OrderItem orderItem =
                orderItemRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDERITEM_NOT_EXISTED));
        OrderItemResponse response = orderItemMapper.toOrderItemResponse(orderItem);
        response.setOrderId(orderItem.getOrder().getId());
        response.setImageUrl(orderItem.getProduct().getImage());
        response.setNameProduct(orderItem.getProduct().getName());
        response.setProductId(orderItem.getProduct().getId());
        response.setSize(orderItem.getSize());
        return response;
    }

    //    @Cacheable(value = "allOrderItems") // Cache toàn bộ danh sách OrderItem
    public List<OrderItemResponse> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        return orderItems.stream()
                .map(orderItem -> {
                    OrderItemResponse response = orderItemMapper.toOrderItemResponse(orderItem);
                    response.setOrderId(orderItem.getOrder().getId());
                    response.setImageUrl(orderItem.getProduct().getImage());
                    response.setNameProduct(orderItem.getProduct().getName());
                    response.setProductId(orderItem.getProduct().getId());
                    response.setSize(orderItem.getSize());
                    return response;
                })
                .toList();
    }
}

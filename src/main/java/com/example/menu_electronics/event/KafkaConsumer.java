package com.example.menu_electronics.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.menu_electronics.dto.response.AskCustomerResponse;
import com.example.menu_electronics.dto.response.OrderResponse;
import com.example.menu_electronics.entity.NotifySystem;
import com.example.menu_electronics.repository.NotifySystemRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumer {

    NotifySystemRepository notifySystemRepository;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationDelivery(AskCustomerResponse message) {
        log.info("Message received: {}", message);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        NotifySystem notifySystem = new NotifySystem();
        notifySystem.setNotificationId(message.getId());
        notifySystem.setTable_name(message.getTable_name());
        notifySystem.setOrderId(null);
        notifySystem.setCreated_at(orderTime.format(formatter));
        notifySystem.setContent("Table " + message.getTable_name() + " sent a request to the shop!");
        notifySystem.setReadNotify(false);
        notifySystemRepository.save(notifySystem);
    }

    @KafkaListener(topics = "notification-order")
    public void listenNotificationOrder(OrderResponse message) {
        log.info("Message received: {}", message);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        NotifySystem notifySystem = new NotifySystem();
        notifySystem.setOrderId(message.getId());
        notifySystem.setTable_name(message.getTable_name());
        notifySystem.setNotificationId(null);
        notifySystem.setCreated_at(orderTime.format(formatter));
        notifySystem.setReadNotify(false);
        if (message.getStatus().equals("DONE")) {
            notifySystem.setContent("The order for code " + message.getId() + " has been completed!");
        } else {
            notifySystem.setContent("Order code " + message.getId() + " dished up successfully!");
        }
        notifySystemRepository.save(notifySystem);
    }
}

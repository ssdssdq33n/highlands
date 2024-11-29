package com.example.menu_electronics.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.menu_electronics.dto.request.AskCustomerRequest;
import com.example.menu_electronics.dto.response.AskCustomerResponse;
import com.example.menu_electronics.entity.AskCustomer;
import com.example.menu_electronics.entity.Selection;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.repository.NotificationRepository;
import com.example.menu_electronics.repository.SelectionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AskCustomerService {
    NotificationRepository notificationRepository;
    SelectionRepository selectionRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    public String creatAsk(AskCustomerRequest notificationRequest) {
        AskCustomer notification = new AskCustomer();
        List<Selection> selection = selectionRepository.findAllById(notificationRequest.getSelectedIds());
        notification.setSelection(selection);
        notification.setDescription(notificationRequest.getDescription());
        notification.setTable_name(notificationRequest.getTable_name());
        notification = notificationRepository.save(notification);
        AskCustomerResponse notificationResponse = new AskCustomerResponse();
        List<String> selectionNames = new ArrayList<>();
        for (Selection s : selection) {
            selectionNames.add(s.getName());
        }
        notificationResponse.setId(notification.getId());
        notificationResponse.setDescription(notification.getDescription());
        notificationResponse.setSelectionNames(selectionNames);
        notificationResponse.setTable_name(notification.getTable_name());
        kafkaTemplate.send("notification-delivery", notificationResponse);
        return "Send notify successfully";
    }

    public AskCustomerResponse getAskById(long id) {
        AskCustomer notification = notificationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));
        AskCustomerResponse notificationResponse = new AskCustomerResponse();
        List<String> selectionNames = new ArrayList<>();
        for (Selection s : notification.getSelection()) {
            selectionNames.add(s.getName());
        }
        notificationResponse.setDescription(notification.getDescription());
        notificationResponse.setId(notification.getId());
        notificationResponse.setSelectionNames(selectionNames);
        notificationResponse.setTable_name(notification.getTable_name());
        return notificationResponse;
    }

    public List<AskCustomerResponse> getAllAsks() {
        List<AskCustomer> notifications = notificationRepository.findAll();
        List<AskCustomerResponse> notificationResponses = new ArrayList<>();
        for (AskCustomer notification : notifications) {
            AskCustomerResponse notificationResponse = new AskCustomerResponse();
            List<String> selectionNames = new ArrayList<>();
            for (Selection s : notification.getSelection()) {
                selectionNames.add(s.getName());
            }
            notificationResponse.setId(notification.getId());
            notificationResponse.setDescription(notification.getDescription());
            notificationResponse.setSelectionNames(selectionNames);
            notificationResponse.setTable_name(notification.getTable_name());
            notificationResponses.add(notificationResponse);
        }
        return notificationResponses;
    }

    public String deleteAskById(long id) {
        AskCustomer notification = notificationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));
        notificationRepository.delete(notification);
        return "Deleted Successfully";
    }
}

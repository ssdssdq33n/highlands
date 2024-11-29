package com.example.menu_electronics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.entity.NotifySystem;
import com.example.menu_electronics.service.NotifySystemService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotifySystemController {

    NotifySystemService notifySystemService;

    @GetMapping("/getAll")
    ApiResponse<List<NotifySystem>> getAllNotify() {
        return ApiResponse.<List<NotifySystem>>builder()
                .result(notifySystemService.getAllNotifySystems())
                .build();
    }

    //    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    //    public Flow.Publisher<String> getAllProducts() {
    //        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
    //
    //        try {
    //            List<NotifySystem> notifySystems = notifySystemService.getAllNotifySystems();
    //            notifySystems.forEach(system -> {
    //                System.out.println("Sending: " + system.toString());
    //                publisher.submit(system.toString());
    //            });
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        } finally {
    //            publisher.close();
    //        }
    //
    //        return publisher;
    //    }

    @GetMapping("/getAllTotal")
    ApiResponse<List<NotifySystem>> getAllNotifyTotal() {
        return ApiResponse.<List<NotifySystem>>builder()
                .result(notifySystemService.getAllNotifySystemsTotal())
                .build();
    }

    @GetMapping("/read/{id}")
    ApiResponse<NotifySystem> readNotify(@PathVariable("id") Long id) {
        return ApiResponse.<NotifySystem>builder()
                .result(notifySystemService.readNotifySystem(id))
                .build();
    }
}

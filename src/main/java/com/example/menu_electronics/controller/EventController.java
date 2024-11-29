// package com.example.menu_electronics.controller;
//
// import com.example.menu_electronics.dto.response.StateMenu;
// import com.example.menu_electronics.entity.authen.User;
// import com.example.menu_electronics.exception.AppException;
// import com.example.menu_electronics.exception.ErrorCode;
// import com.example.menu_electronics.repository.authenRepository.UserRepository;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;
// import java.util.concurrent.Executors;
// import java.util.concurrent.ScheduledExecutorService;
// import java.util.concurrent.TimeUnit;
//
// @RestController
// public class EventController {
//
//    private final UserRepository userRepository;
//    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public EventController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @GetMapping("/events/{id}")
//    public SseEmitter streamEvents(@PathVariable String id) {
//        SseEmitter emitter = new SseEmitter(3600000L);
//        emitters.put(id, emitter);
//
//        emitter.onCompletion(() -> emitters.remove(id));
//        emitter.onError((Throwable t) -> emitters.remove(id));
//
//        // Gửi trạng thái người dùng ngay khi kết nối
//        sendStateUpdate(id, emitter);
//
//        return emitter;
//    }
//
//    // Phương thức để thay đổi trạng thái người dùng
//    @GetMapping("/toggle")
//    public void toggleUserState() {
//        var context = SecurityContextHolder.getContext();
//        String name = context.getAuthentication().getName();
//
//        User user = userRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        // Giả sử bạn có phương thức để thay đổi trạng thái
//        user.setState(!user.getState());
//        userRepository.save(user);
//
//        // Gửi thông báo đến tất cả các client đang lắng nghe
//        emitters.forEach((key, emitter) -> sendStateUpdate(key, emitter));
//    }
//
//    private void sendStateUpdate(String id, SseEmitter emitter) {
//        try {
//            User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//            StateMenu stateMenu = new StateMenu();
//            stateMenu.setState(user.getState());
//            String jsonResponse = objectMapper.writeValueAsString(stateMenu);
//            emitter.send(jsonResponse);
//        } catch (IOException e) {
//            emitter.completeWithError(e);
//            emitters.remove(id);
//        }
//    }
// }

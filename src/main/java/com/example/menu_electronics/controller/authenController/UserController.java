package com.example.menu_electronics.controller.authenController;

import java.util.List;

import com.example.menu_electronics.dto.response.authenResponse.AuthenticationResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.dto.request.authenRequest.UserCreationRequest;
import com.example.menu_electronics.dto.request.authenRequest.UserUpdateRequest;
import com.example.menu_electronics.dto.response.authenResponse.UserResponse;
import com.example.menu_electronics.service.authenService.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
// @CrossOrigin("*")
public class UserController {
    UserService userService;

    @PostMapping("/registration")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PostMapping("/create")
    ApiResponse<AuthenticationResponse> createCustomer(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(userService.createCustomer(request))
                .build();
    }

    @GetMapping("/forgot/{email}")
    ApiResponse<UserResponse> forgotPassword(@PathVariable("email") String email) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.forgotPassword(email))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(@RequestParam Boolean isCustomer) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers(isCustomer))
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("/sendPayment")
    ApiResponse<String> sendPaymentEmail() {
        return ApiResponse.<String>builder()
                .result(userService.sendEmailPayment())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @GetMapping("/changePassword/{userId}")
    ApiResponse<UserResponse> updatePasswordUser(
            @PathVariable String userId,
            @RequestParam("password") String password,
            @RequestParam("passwordOld") String passwordOld) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updatePasswordUser(userId, passwordOld, password))
                .build();
    }

    @GetMapping("/changeName/{userId}")
    ApiResponse<UserResponse> updateNameUser(@PathVariable String userId, @RequestParam("name") String name) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateNameUser(userId, name))
                .build();
    }
}

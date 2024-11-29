package com.example.menu_electronics.controller.authenController;

import java.text.ParseException;

import org.springframework.web.bind.annotation.*;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.dto.request.authenRequest.AuthenticationRequest;
import com.example.menu_electronics.dto.request.authenRequest.IntrospectRequest;
import com.example.menu_electronics.dto.request.authenRequest.LogoutRequest;
import com.example.menu_electronics.dto.request.authenRequest.RefreshRequest;
import com.example.menu_electronics.dto.response.authenResponse.AuthenticationResponse;
import com.example.menu_electronics.dto.response.authenResponse.IntrospectResponse;
import com.example.menu_electronics.service.authenService.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// @CrossOrigin("*")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}

package com.example.diplom.controller;

import com.example.diplom.entity.AuthRequest;
import com.example.diplom.entity.AuthResponse;
import com.example.diplom.entity.UserEntity;
import com.example.diplom.model.UserCreateResponse;
import com.example.diplom.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@Validated
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok()
                .body(loginService.authorizationUser(authRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("auth-token") String authToken) {
        return ResponseEntity.ok()
                .body(loginService.logOutUser(authToken));
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserEntity userEntity) {
        return ResponseEntity.ok()
                .body(loginService.createUser(userEntity));
    }
}
package com.example.diplom.controller;

import com.example.diplom.entity.AuthRequest;
import com.example.diplom.entity.AuthResponse;
import com.example.diplom.entity.UserEntity;
import com.example.diplom.model.UserCreateResponse;
import com.example.diplom.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@Validated
public class LoginController {
    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/cloud/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok()
                .body(loginService.authorizationUser(authRequest));
    }

    @PostMapping("/cloud/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("auth-token") String authToken) {
        return ResponseEntity.ok()
                .body(loginService.logOutUser(authToken));
    }

    @PostMapping("/cloud/createUser")
    public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserEntity userEntity) {
        return ResponseEntity.ok()
                .body(loginService.createUser(userEntity));
    }
}
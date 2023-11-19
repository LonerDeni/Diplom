package com.example.diplom.controller;

import com.example.diplom.entity.AuthRequest;
import com.example.diplom.entity.AuthResponse;
import com.example.diplom.entity.UserEntity;
import com.example.diplom.exception.AuthTokenException;
import com.example.diplom.exception.FileException;
import com.example.diplom.service.LoginService;
import com.example.diplom.service.LoginServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@Validated
public class LoginController {
    LoginService loginService;


    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/cloud/login")
    public AuthResponse loginUser(@Valid @RequestBody AuthRequest authRequest) {
//        try {
            return loginService.authorizationUser(authRequest);
//        } catch (Exception e) {
//            throw new AuthTokenException("Not authorized");
//        }
    }

    @PostMapping("/cloud/logout")
    public String logoutUser(@RequestHeader("auth-token") String authToken) {
        return loginService.logOutUser(authToken);
    }

    @PostMapping("/cloud/createUser")
    public String createUser(@Valid @RequestBody UserEntity userEntity) {
        return "user";
    }
}
package com.example.diplom.service;

import com.example.diplom.entity.AuthRequest;
import com.example.diplom.entity.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    AuthResponse authorizationUser(AuthRequest authRequest);
    String logOutUser(String token);

}

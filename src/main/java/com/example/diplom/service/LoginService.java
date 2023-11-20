package com.example.diplom.service;

import com.example.diplom.entity.AuthRequest;
import com.example.diplom.entity.AuthResponse;
import com.example.diplom.entity.UserEntity;
import com.example.diplom.model.UserCreateResponse;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    AuthResponse authorizationUser(AuthRequest authRequest);
    String logOutUser(String token);
    UserCreateResponse createUser(UserEntity userEntity);

}

package com.example.diplom.service;

import com.example.diplom.entity.AuthRequest;
import com.example.diplom.entity.AuthResponse;
import com.example.diplom.entity.UserEntity;
import com.example.diplom.exception.AuthTokenException;
import com.example.diplom.exception.CreateUserException;
import com.example.diplom.model.UserCreateResponse;
import com.example.diplom.repository.UserRepositories;
import com.example.diplom.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepositories userRepositories;
    private final JWTUtil jwtUtil;

    @Override
    public AuthResponse authorizationUser(AuthRequest authRequest) {
        if (isNull(authRequest.getLogin()) && isNull(authRequest.getPassword())) {
            log.error("Login or password is empty");
            throw new AuthTokenException("login or password not sent");
        }
        UserEntity userAuth = userRepositories.findByLogin(authRequest.getLogin()).orElseThrow(()
                -> new AuthTokenException("User not found"));
        log.info("Search user");
        String authPas = encodePassword(authRequest.getPassword());
        if (!userAuth.getPassword().equals(authPas)) {
            log.error("Password not valid");
            throw new AuthTokenException("Incorrect password.");
        }
        String token = jwtUtil.generateJwtToken(authRequest);
        log.info("Create user token successful");
        return new AuthResponse(token);
    }

    @Override
    @Cacheable(cacheNames = "jwtCache")
    public String logOutUser(String token) {
        jwtUtil.validateJwtToken(token);
        log.info("Logout user successful");
        return "Success logout";
    }

    @Override
    public UserCreateResponse createUser(UserEntity userEntity) {
        Optional<UserEntity> newUser = userRepositories.findByLoginOrAndEmail(userEntity.getLogin(), userEntity.getEmail());
        if (newUser.isPresent()) {
            log.error("User with login is create");
            throw new CreateUserException("User with login already exists");
        }
        String encodePas = encodePassword(userEntity.getPassword());
        userEntity.setPassword(encodePas);
        userRepositories.save(userEntity);
        log.info(String.format("New User is create %s",userEntity.getLogin()));
        return new UserCreateResponse("Create new user" + userEntity.getLogin());
    }

    private String encodePassword(String password) {
        log.info("encode password");
        String saltPass = "rabbit";
        return DigestUtils.md5Hex(password + saltPass);
    }
}
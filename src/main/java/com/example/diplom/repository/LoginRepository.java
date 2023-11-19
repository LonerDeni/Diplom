package com.example.diplom.repository;

import com.example.diplom.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LoginRepository {

    private final UserRepositories userRepositories;

    public LoginRepository (UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }
    public Optional<UserEntity> checkUser(String login){
        return userRepositories.findByLogin(login);
    }

}

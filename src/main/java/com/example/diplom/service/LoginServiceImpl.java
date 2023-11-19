package com.example.diplom.service;

import com.example.diplom.entity.AuthRequest;
import com.example.diplom.entity.AuthResponse;
import com.example.diplom.entity.UserEntity;
import com.example.diplom.exception.AuthTokenException;
import com.example.diplom.repository.LoginRepository;
import com.example.diplom.repository.UserRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final LoginRepository loginRepository;
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
        if(!userAuth.getPassword().equals(authPas)){
            log.error("Password not valid");
            throw new AuthTokenException("Incorrect password.");
        }
        String token = jwtUtil.generateJwtToken(authRequest);
        log.info("Create user token successful");
       return new AuthResponse(token);
    }

    @Override
    public String logOutUser(String token) {
        log.info("Logout user successful");
        return null;
    }

    //    public void loginUser(){
//        String saltPass = "rabbit";
//        String passwordHex =  DigestUtils.md5Hex(userEntity.getPassword() + saltPass);
//        Optional<UserEntity> actualUser = loginRepository.checkUser(userEntity.getLogin());
//        if(actualUser.isEmpty()){
//            throw new ArrayIndexOutOfBoundsException("Такой пользователь не найден");
//        }
//        if(!actualUser.get().getPassword().equals(passwordHex)){
//            throw new ArrayIndexOutOfBoundsException("Пароль не верный");
//        }
//        return "Success authorization";
//    }
//
    private String encodePassword(String password){
        log.info("encode password");
        String saltPass = "rabbit";
        return DigestUtils.md5Hex(password + saltPass);
    }
//    private String decodePassword(String password){
//        String saltPass = "rabbit";
//        byte[] shalHash = DigestUtils.md5(password.getBytes());
//        byte[] encoded = Base64.encodeBase64(shalHash);
//        String encodePs = encoded.toString();
//        return encodePs;
//    }
}

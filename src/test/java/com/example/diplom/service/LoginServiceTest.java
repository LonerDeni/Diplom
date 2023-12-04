package com.example.diplom.service;

import com.example.diplom.entity.UserEntity;
import com.example.diplom.repository.UserRepositories;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static com.example.diplom.service.PrepareInfoForTest.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@SpringBootTest
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
public class LoginServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepositories userRepositories;


    @AfterEach
    public void cleanTestUserData(){
         userRepositories.deleteAll();
    }

    @BeforeEach
    public void addUser(){
        userRepositories.save(new UserEntity("testUnitData","5dfc91e02ecc53592738b6c36eedb875","Ivan","Petrov","testUserData@test.com"));
    }

    @Test
    public void authSuccessTest() throws Exception{
        mockMvc.perform(multipart("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\": \"testUnitData\", \"password\": \"test123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void authNotValidLoginTest() throws Exception{
        mockMvc.perform(multipart("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"qwery\", \"password\": \"test123\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":null,\"message\":\"User not found\"}"));
    }
    @Test
    public void authNotValidPassTest() throws Exception{
        mockMvc.perform(multipart("/cloud/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"testUnitData\", \"password\": \"qwerty\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":null,\"message\":\"Incorrect password.\"}"));
    }
    @Test
    public void logoutTest() throws Exception{
        mockMvc.perform(multipart("/cloud/logout")
                .header("auth-token", createTestToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Success logout"));
    }
    @Test
    public void logoutNotValidTest() throws Exception{
        mockMvc.perform(multipart("/cloud/logout")
                        .header("auth-token", "dfsdf"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":null,\"message\":\"Invalid Jwt token\"}"));
    }

    @Test
    public void addUserTest() throws Exception{
        mockMvc.perform(multipart("/cloud/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"login\" : \"test777\",\n" +
                                "    \"password\" : \"test12399\",\n" +
                                "    \"name\" : \"qwe\",\n" +
                                "    \"lastName\" : \"qwe\",\n" +
                                "    \"email\" : \"test777@test.ru\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Optional<UserEntity> entity = userRepositories.findByLogin("test777");
        assertTrue(entity.isPresent());
    }
}

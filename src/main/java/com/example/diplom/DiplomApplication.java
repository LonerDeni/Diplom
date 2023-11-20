package com.example.diplom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiplomApplication {
 //TODO
 // +1)Разобраться со сборкой liquibase
 // +2) Разобраться с выводом ошибок при авторизации
 // +-3) Реализовать logout через проверка в list
 // 4) Написать docker file и интеграциионные тесты
    public static void main(String[] args) {
        SpringApplication.run(DiplomApplication.class, args);
    }

}

package com.example.diplom.repository;

import com.example.diplom.entity.UserEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositories extends JpaRepository<UserEntity,Long> {
        @Query(value = "select e from User e where upper(e.login) = upper(:login)")
        Optional<UserEntity> findByLogin(@Param("login") String login);

        @Query(value = "select e from User e where upper(e.login) = upper(:login) and upper(e.email) = upper(:email)")
        Optional<UserEntity> findByLoginOrAndEmail(@Param("login") String login, @Param("email") String email);

}

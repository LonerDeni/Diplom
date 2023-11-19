package com.example.diplom.repository;

import com.example.diplom.entity.FileEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepositories extends JpaRepository<FileEntity,Long>{
        @Query(value = "select e from FileEntity e where upper(e.fileName) = upper(:fileName)")
        FileEntity findByFileName(String fileName);

        @Query(value = "select e from FileEntity e where upper(e.fileName) = upper(:fileName) and e.userId = :userId")
        FileEntity findByUserIdAndFileName(Long userId,String fileName);
        List<FileEntity> findByUserId(Long userId);
}
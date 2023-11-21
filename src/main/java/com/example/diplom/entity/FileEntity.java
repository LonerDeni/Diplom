package com.example.diplom.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.relational.core.sql.In;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "netology", name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String path;

    @Column(name = "size_file")
    private Long size;

    @Column(name = "modify_date")
    @CreationTimestamp
    private Instant modifyDate;

    @Column(name = "userid")
    private Long userId;

    public FileEntity(String fileName, Long userId, String path, Long size) {
        this.fileName = fileName;
        this.userId = userId;
        this.path = path;
        this.size = size;
    }
}
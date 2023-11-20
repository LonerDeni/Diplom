package com.example.diplom.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(schema = "netology",name = "userstest")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String login;
    @NotNull
    private String password;

    private String name;
    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "modify_date")
    @CreationTimestamp
    private Instant modifyDate;

    @OneToMany()
    @JoinColumn(name = "userid")
    private List<FileEntity> files;
}
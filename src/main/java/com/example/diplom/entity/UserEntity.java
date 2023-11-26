package com.example.diplom.entity;

import lombok.*;
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
@AllArgsConstructor
@Table(schema = "netology",name = "users")
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

    public UserEntity(String login, String password, String name, String lastName, String email) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }
}
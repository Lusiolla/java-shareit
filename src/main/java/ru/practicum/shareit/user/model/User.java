package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    private String name;
}
package ru.practicum.shareit.requests.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "user_id", nullable = false)
    private Long userId;
}

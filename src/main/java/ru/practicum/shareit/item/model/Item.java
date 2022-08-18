package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "is_available", nullable = false)
    private Boolean available;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @OneToMany
    @JoinColumn(name = "item_id")
    private Collection<Comment> comments;
}

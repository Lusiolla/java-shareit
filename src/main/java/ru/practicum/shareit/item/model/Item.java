package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(name = "request_id")
    private Long requestId;
    @OneToMany
    @JoinColumn(name = "item_id")
    private Collection<Comment> comments;
}

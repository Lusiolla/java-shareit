package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items_comments", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"item_id", "author_id"}))
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String text;
    @Column(name = "item_id", nullable = false)
    private long itemId;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User user;
    LocalDateTime created;


}

package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

@Component
public class CommentMapper {

    // из comment в dto
    public CommentDto mapToCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getUser().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }

    // из dto в comment
    public Comment mapToComment(CommentDto dto, long userId, long itemId) {
        Comment comment = new Comment();
        User user = new User();
        user.setId(userId);
        comment.setId(dto.getId());
        comment.setText(dto.getText());
        comment.setItemId(itemId);
        comment.setUser(user);
        return comment;
    }
}

package com.yapai.dreamcatcher.mapper;

import com.yapai.dreamcatcher.dto.CommentDto;
import com.yapai.dreamcatcher.entity.Comment;

public class CommentMapper {

    public static CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setComment(comment.getComment());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setDreamId(comment.getDream().getId());
        return commentDto;
    }

}

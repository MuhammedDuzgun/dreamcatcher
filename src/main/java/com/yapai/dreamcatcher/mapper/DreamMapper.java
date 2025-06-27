package com.yapai.dreamcatcher.mapper;

import com.yapai.dreamcatcher.dto.DreamDto;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class DreamMapper {

    public static DreamDto mapToDreamDto(com.yapai.dreamcatcher.entity.Dream dream) {
        DreamDto dreamDto = new DreamDto();
        dreamDto.setId(dream.getId());
        dreamDto.setDream(dream.getDream());
        dreamDto.setDreamInterpretation(dream.getDreamInterpretation());
        dreamDto.setUserId(dream.getUser().getId());
        if (dream.getComments() != null) {
            dreamDto.setComments(dream.getComments().stream().map(CommentMapper::mapToCommentDto).toList());
        }
        LocalDateTime formattedDate = dateFormatter(dream.getCreatedAt());
        dreamDto.setCreatedAt(formattedDate);
        return dreamDto;
    }

    private static LocalDateTime dateFormatter(LocalDateTime date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setLenient(false);
        formatter.format(date);
        return date;
    }

}

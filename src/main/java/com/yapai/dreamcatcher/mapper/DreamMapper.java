package com.yapai.dreamcatcher.mapper;

import com.yapai.dreamcatcher.dto.DreamDto;

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
        return dreamDto;
    }

}

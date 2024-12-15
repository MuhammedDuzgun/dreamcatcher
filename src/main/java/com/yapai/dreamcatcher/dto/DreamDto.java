package com.yapai.dreamcatcher.dto;

import java.util.List;

public class DreamDto {

    private Long id;
    private String dream;
    private String dreamInterpretation;
    private Long userId;
    private List<CommentDto> comments;

    public DreamDto() {
    }

    public DreamDto(Long id, String dream, String dreamInterpretation, Long userId, List<CommentDto> comments) {
        this.id = id;
        this.dream = dream;
        this.dreamInterpretation = dreamInterpretation;
        this.userId = userId;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDream() {
        return dream;
    }

    public void setDream(String dream) {
        this.dream = dream;
    }

    public String getDreamInterpretation() {
        return dreamInterpretation;
    }

    public void setDreamInterpretation(String dreamInterpretation) {
        this.dreamInterpretation = dreamInterpretation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }
}

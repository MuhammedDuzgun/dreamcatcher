package com.yapai.dreamcatcher.dto;

public class CommentDto {

    private Long id;
    private String comment;
    private Long dreamId;
    private Long userId;

    public CommentDto() {
    }

    public CommentDto(Long id, String comment, Long dreamId, Long userId) {
        this.id = id;
        this.comment = comment;
        this.dreamId = dreamId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getDreamId() {
        return dreamId;
    }

    public void setDreamId(Long dreamId) {
        this.dreamId = dreamId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

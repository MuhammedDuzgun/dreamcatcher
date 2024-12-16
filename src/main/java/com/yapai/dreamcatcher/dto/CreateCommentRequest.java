package com.yapai.dreamcatcher.dto;

public class CreateCommentRequest {

    private String comment;
    private Long dreamId;

    public CreateCommentRequest() {
    }

    public CreateCommentRequest(String comment, Long dreamId) {
        this.comment = comment;
        this.dreamId = dreamId;
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
}

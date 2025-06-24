package com.yapai.dreamcatcher.exception;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

public class ErrorResponse {

    private int status;
    private String message;

    @CreationTimestamp
    private LocalDateTime creationTime;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}

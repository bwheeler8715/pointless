package com.bwheeler8715.pointless.json;

import java.time.OffsetDateTime;

public class ErrorResponseJson {
    private int stausCode;
    private String status;
    private String message;
    private String path;
    private OffsetDateTime timestamp;

    public int getStausCode() {
        return stausCode;
    }

    public void setStausCode(int stausCode) {
        this.stausCode = stausCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
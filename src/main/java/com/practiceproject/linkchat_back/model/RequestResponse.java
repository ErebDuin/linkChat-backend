package com.practiceproject.linkchat_back.model;

public class RequestResponse {
    private String message;

    public RequestResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String setMessage(String message) {
        return this.message = message;
    }
}

package com.github.library_app_boot.util;

public class MemberErrorResponse {
    private String message;

    public MemberErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

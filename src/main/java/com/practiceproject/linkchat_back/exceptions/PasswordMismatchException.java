package com.practiceproject.linkchat_back.exceptions;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Passwords do not match");
    }
}

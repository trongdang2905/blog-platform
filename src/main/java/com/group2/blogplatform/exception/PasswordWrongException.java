package com.group2.blogplatform.exception;

public class PasswordWrongException extends RuntimeException {
    public PasswordWrongException(String message) {
        super(message);
    }
}

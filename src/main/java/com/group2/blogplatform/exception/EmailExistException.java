package com.group2.blogplatform.exception;


public class EmailExistException extends RuntimeException {
    public EmailExistException(String message) {
        super(message);
    }
}
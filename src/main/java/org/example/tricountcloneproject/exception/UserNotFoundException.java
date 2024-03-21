package org.example.tricountcloneproject.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Unregistered User");
    }
}

package org.example.tricountcloneproject.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity) {
        super("Cannot find " + entity);
    }
}

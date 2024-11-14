package ru.madjo.exceptions;

public class VersionAlreadyExistsException extends RuntimeException {
    public VersionAlreadyExistsException(String message, Throwable ex) { super(message, ex);}
    public VersionAlreadyExistsException(String message) { super(message);}
}

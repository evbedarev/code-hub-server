package ru.madjo.exceptions;

public class FilePathNotDefinedException extends RuntimeException {
    public FilePathNotDefinedException(String message, Throwable ex) { super(message, ex);}
    public FilePathNotDefinedException(String message) { super(message);}
}

package ru.madjo.exceptions;

public class ProjectCodeNotFoundException extends RuntimeException {

    public ProjectCodeNotFoundException(String message, Throwable ex) { super(message, ex);}

    public ProjectCodeNotFoundException(String message) { super(message);}
}

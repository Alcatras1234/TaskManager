package org.example.task_manager.exceptionHandler;

public class UnauthorizedException extends Exception{
    public UnauthorizedException(String message) {
        super(message);
    }
}

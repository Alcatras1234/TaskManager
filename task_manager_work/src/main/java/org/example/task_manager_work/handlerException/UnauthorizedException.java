package org.example.task_manager_work.handlerException;

public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}

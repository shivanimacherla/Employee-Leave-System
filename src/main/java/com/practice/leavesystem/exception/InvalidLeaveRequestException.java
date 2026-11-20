package com.practice.leavesystem.exception;

public class InvalidLeaveRequestException extends RuntimeException {
    public InvalidLeaveRequestException(String message) {
        super(message);
    }
}

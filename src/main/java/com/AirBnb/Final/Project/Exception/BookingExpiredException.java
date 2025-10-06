package com.AirBnb.Final.Project.Exception;

public class BookingExpiredException extends RuntimeException {
    public BookingExpiredException(String message) {
        super(message);
    }
}

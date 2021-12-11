package com.hashedin.virtualproperty.application.exceptions;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message){
        super(message);
    }
}

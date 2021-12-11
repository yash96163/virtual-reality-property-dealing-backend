package com.hashedin.virtualproperty.application.exceptions;

public class InvalidRequest extends RuntimeException{
    public InvalidRequest(String message){
        super(message);
    }
}
